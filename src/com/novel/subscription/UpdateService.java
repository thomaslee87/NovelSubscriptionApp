package com.novel.subscription;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.novel.subscription.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class UpdateService extends Service {
	
	private NotificationManager _nm;
	 
	NovelDB db ;
    ReadUpdateDataTask task;
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
    	Context context = getApplicationContext();
    	task = new ReadUpdateDataTask(this);
    	
        _nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//	        showNotification();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        task.start();
//        showNotification();
        super.onStart(intent, startId);
        showNotification();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        task.stop();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        // TODO Auto-generated method stub
        super.onRebind(intent);
    }
    /**
     * Show a notification while this service is running.
     */
    public void showNotification() {
        Notification notification = new Notification(R.drawable.ic_launcher, "您订阅的小说有更新啦",System.currentTimeMillis());
        //设置flags值不等于后面的值。那么点击通知的话 通知会消失。反之则不会消失。
        notification.flags |= Notification.FLAG_AUTO_CANCEL; 
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);    //点击下拉出来的列表后需要跳转的页面
        notification.setLatestEventInfo(this, "通知","有更新", contentIntent);
        _nm.notify(2313, notification);
    }
    
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if(msg.what == 1) {
//        		isRunning = true;
        		showNotification();
        		
        		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
        	}
        }
    };
    
    public class ReadUpdateDataTask {
    	private Context context;
    	private long period = 30 * 1000;

    	public ReadUpdateDataTask(Context context) {
    		this.context = context;
    	}
    	
    	private Timer _mTimer = null;
    	private boolean isStart = false;
    	
    	public void start() {
    		if(!isStart) {
    			isStart = true;
    			_mTimer = new Timer();
    			_mTimer.schedule(new TaskTimer(context), period, period);
    		}
    	}
    	
    	public void stop(){
            if(isStart){
                isStart = false;
                _mTimer.cancel();
            }
        }
    	
    	class TaskTimer extends TimerTask {
        	public boolean isRunning;
        	public boolean firstStartup;
        	public int position;
        	
        	private Context context;
        	private ArrayList<SubscriptionEntity> items = new ArrayList<SubscriptionEntity>();
        	
        	public TaskTimer(Context context) {
        		super();
        		this.context = context;
        	}

                @Override
                public void run()
                {
                	isRunning = true;
                	NovelDB db = new NovelDB(getApplicationContext());

                	Queue<BookChapterEntity> chapterQueue = new LinkedList<BookChapterEntity>();
                	
                	boolean needUpdateDb = true;//false;
                	int curPage = 0, totalPage = 0;
                	int fetchPageNum = -1;
                	for(int i = 0; i < items.size(); i ++) {

                		if(position != -1 && i != position)
                			continue;
                		
                		SubscriptionEntity subscription = items.get(i);
                		BookEntity book = subscription.getBook();
                		
                		fetchPageNum = -1;
                		curPage = 0;
                		totalPage = 0;
                		
                		Pattern pChapter = Pattern.compile(book.getSrcEntity().getPatternChapter());
                    	Pattern pBody = Pattern.compile(book.getSrcEntity().getPatternBody());
                    	Pattern pOrder = Pattern.compile(book.getSrcEntity().getPatternOrder());
                    	Pattern pLatestTitle = Pattern.compile(book.getSrcEntity().getPatternLatestTitle());
                    	Pattern pPage = Pattern.compile(book.getSrcEntity().getPatternPage());
                    	Pattern pInvalidTitle = Pattern.compile("<.*>.*</.*>");
                		
                		//从最后一页开始查找，即从最新开始找
                		do{
        	        		String res = HtmlService.getHtmlByGet(book.getSrcEntity().getDomain() + book.getUrl() + "&pi=" + fetchPageNum);
        	            	
        	            	Matcher mLatestTitle = pLatestTitle.matcher(res);
        	        		if(mLatestTitle.find()) {
        	        			if(mLatestTitle.group(1).equals(subscription.getLatestTitle()))
        	        				continue;
        	        			else
        	        				needUpdateDb = true;
        	//        			Message msg = handler.obtainMessage();
        	//        			msg.what = UPDATING_CODE;
        	//        			Bundle bundle = new Bundle();
        	//        			bundle.putInt("id", subscription.getId());
        	//        			bundle.putString("title", mLatestTitle.group(1));
        	//        			msg.setData(bundle);
        	//        			handler.sendMessage(msg);
        	        		}
        	            	
        	        		Matcher mBody = pBody.matcher(res);
        	    			String url = "";
        	    			String title = "";
        	    			String lastUrl, lastTitle;
        	        		if(mBody.find()) {
        	        			String body = mBody.group(1);
        	            		Matcher m = pChapter.matcher(body);
        	            		while(m.find()){// && isRunning) {
//        	            			if(chapterQueue.size() >= UPDATE_LIST_COUNT) {
//        	            				chapterQueue.remove();
//        	            				fetchPageNum = 0;
//        	            				break;
//        	            			}
        	            			lastUrl = url;
        	            			lastTitle = title;
        	            			
        	            			url = m.group(1).replaceAll("&amp;", "&");
        	            			title = m.group(2);
        	            			if(pInvalidTitle.matcher(title).find())
        	            				continue;
        	            			Matcher mUrlOrder = pOrder.matcher(url);
        	            			if(mUrlOrder.find()) {
        	            				int chapterOrder = 0;
        	            				try {
        	            					chapterOrder = Integer.parseInt(mUrlOrder.group(1));
        	            				}catch (Exception e) {
        	            					Log.i("test", book.getName() + " " + mUrlOrder.group());
        	            					url = lastUrl;
        	            					title = lastTitle;
        	            					continue;
        	            				}
        	            				if(chapterOrder > book.getUpdateOrder())
        	            					chapterQueue.add(new BookChapterEntity(-1, title, url, 0, 0, 0, chapterOrder, 1, book));
        	            				else 
        	            					fetchPageNum = 0; // 到了上次更新的地方，说明读完本页就可以结束循环了
        	            			}
        	            		}
        	            		if(totalPage == 0 && !url.equals("")) {
        	            			ContentValues values = new ContentValues();
        	            			values.put(NovelDB.SUBSCRIPTION_LATEST_TITLE, title);
        	            			values.put(NovelDB.SUBSCRIPTION_LATEST_URL, url);
        	            			db.update(NovelDB.SUBSCRIPTION_TABLE, values, "_id = " + subscription.getId(), null);
        	            		}
        	        		}
        	        		if(totalPage == 0) {
        	        			curPage = 1;
        	        			totalPage = 1;
        		        		Matcher mPage = pPage.matcher(res);
        		        		if(mPage.find()) {
        		        			curPage = Integer.parseInt(mPage.group(1));
        		        			totalPage = Integer.parseInt(mPage.group(2));
        		        		}
        		        		fetchPageNum = totalPage;
        	        		}
        	        		fetchPageNum --;
                		} while (fetchPageNum > 0);
                		
                	}
                	
                	if(needUpdateDb) {
                		db.beginTransaction();
        		        try {
        		        	while(!chapterQueue.isEmpty()) 
        		        		db.addChapter(chapterQueue.remove());
        		        	db.setTransactionSuccessful();
        		        } finally {
        		        	db.endTransaction();
        		        	db.close();
        		        }
                	}
                	else {
                		db.close();
                	}
        	        		
                	isRunning = false;
                	handler.sendEmptyMessage(1);
                }
            }

    	}

}
