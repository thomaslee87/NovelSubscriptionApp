package com.novel.subscription;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adsmogo.adview.AdsMogoLayout;
import com.baidu.mobstat.StatService;

public class BookReaderActivity extends Activity {
	
	private WebView wvBookDisplayer;
	private LinearLayout lyLoading;
	private BookChapterEntity bookChapter;
	
	private Button btnReload;
	private TextView tvTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
		setContentView(R.layout.book_reader);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.reader_title);
				
		wvBookDisplayer = (WebView) findViewById(R.id.bookDisplayer);
		wvBookDisplayer.setBackgroundColor(0);
		
		lyLoading = (LinearLayout) findViewById(R.id.loading);
		
		bookChapter = (BookChapterEntity)this.getIntent().getSerializableExtra("bookChapter");
//		setTitle(bookChapter.getBook().getName() + " " + bookChapter.getChapterTitle());
		tvTitle = (TextView) getWindow().findViewById(R.id.tvTitle);
		tvTitle.setText(bookChapter.getBook().getName() + " " + bookChapter.getChapterTitle());
		
		WebSettings wvBookDisplayerSettings = wvBookDisplayer.getSettings();
		wvBookDisplayerSettings.setDefaultFontSize(20);
//		wvBookDisplayer.setBackgroundColor(0x999999);
		
		btnReload = (Button) getWindow().findViewById(R.id.btnReload);
//		btnReload = (Button) findViewById(R.id.btnReload);
		btnReload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lyLoading.setVisibility(View.VISIBLE); 
				thread = new ProgressThread();
				thread.start();
			}
		});
		
		showChapterContent(true);
		
		adsMogoLayoutCode = new AdsMogoLayout(this, MainActivity.MogoID);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
			FrameLayout.LayoutParams.WRAP_CONTENT,
			FrameLayout.LayoutParams.WRAP_CONTENT);
		params.bottomMargin = 0;
		params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
		addContentView(adsMogoLayoutCode, params);
//		
//		mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer2);
//		
//		Calendar now = Calendar.getInstance();
//		int hour = now.get(Calendar.HOUR_OF_DAY);
//		long timeRandom = now.getTimeInMillis() / 1000;
//		if(true) {//timeRandom % 2 == 0) {
//			AdView youmiAdView = new AdView(this, AdSize.SIZE_320x50);
//	        mAdContainer.addView(youmiAdView);
//
//	        // 监听广告条接口
//	        youmiAdView.setAdListener(new AdViewLinstener() {
//	        	
//	            @Override
//	            public void onSwitchedAd(AdView arg0) {
//	                Log.i("YoumiSample", "广告条切换");
//	            }
//	            
//	            @Override
//	            public void onReceivedAd(AdView arg0) {
//	                Log.i("YoumiSample", "请求广告成功");
//	                
//	            }
//	            
//	            @Override
//	            public void onFailedToReceivedAd(AdView arg0) {
//	                Log.i("YoumiSample", "请求广告失败");
//	            }
//	            
//	        });
//	        
//	        youmiAdView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Log.d("com.novel.subscription", "click ad");
//					Calendar now = Calendar.getInstance();
//			    	now.set(Calendar.HOUR_OF_DAY, 0);
//			    	now.set(Calendar.MINUTE, 0);
//			    	now.set(Calendar.SECOND, 0);
//					
//					Context ctx = getApplicationContext();
//					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//					
//			   		Editor editor = prefs.edit();
//					editor.putLong(ConstDefinition.LAST_CLK_AD_TIME, now.getTimeInMillis());
//					editor.commit();
//					
//					mAdContainer.setVisibility(View.GONE);
//				}
//			});
//		}
//		else {
			/*
			// Create ad view
			mAdview320x50 = new DomobAdView(this, MainActivity.PUBLISHER_ID, MainActivity.InlinePPID, DomobAdView.INLINE_SIZE_320X50);
			if(hour >= 20)
				mAdview320x50.setKeyword("game");
	//		mAdview320x50.setUserGender("male");
	//		mAdview320x50.setUserBirthdayStr("2000-08-08");
	//		mAdview320x50.setUserPostcode("123456");
	
			mAdview320x50.setAdEventListener(new DomobAdEventListener() {
							
				@Override
				public void onDomobAdReturned(DomobAdView adView) {
					Log.i("DomobSDKDemo", "onDomobAdReturned");				
				}
	
				@Override
				public void onDomobAdOverlayPresented(DomobAdView adView) {
					Log.i("DomobSDKDemo", "overlayPresented");
				}
	
				@Override
				public void onDomobAdOverlayDismissed(DomobAdView adView) {
					Log.i("DomobSDKDemo", "Overrided be dismissed");			
					
					Calendar now = Calendar.getInstance();
			    	now.set(Calendar.HOUR_OF_DAY, 0);
			    	now.set(Calendar.MINUTE, 0);
			    	now.set(Calendar.SECOND, 0);
					
					Context ctx = getApplicationContext();
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
					
			   		Editor editor = prefs.edit();
					editor.putLong(ConstDefinition.LAST_CLK_AD_TIME, now.getTimeInMillis());
					editor.commit();
					
	//				mAdContainer.setVisibility(View.GONE);
				}
	
				@Override
				public void onDomobAdClicked(DomobAdView arg0) {
					Log.i("DomobSDKDemo", "onDomobAdClicked");				
				}
	
				@Override
				public void onDomobAdFailed(DomobAdView arg0, ErrorCode arg1) {
					Log.i("DomobSDKDemo", "onDomobAdFailed");				
				}
	
				@Override
				public void onDomobLeaveApplication(DomobAdView arg0) {
					Log.i("DomobSDKDemo", "onDomobLeaveApplication");				
				}
	
				@Override
				public Context onDomobAdRequiresCurrentContext() {
					return BookReaderActivity.this;
				}
			});
			
			mAdContainer.addView(mAdview320x50);
			*/
//		}
		
	}
	
	private AdsMogoLayout adsMogoLayoutCode;
	
	@Override
	protected void onDestroy() {
		// 清除 adsMogoLayout 实例 所产生用于多线程缓冲机制的线程池
		// 此方法请不要轻易调用，如果调用时间不当，会造成无法统计计数
		if (adsMogoLayoutCode != null) {
			adsMogoLayoutCode.clearThread();
		}
		super.onDestroy();
	}
			
	RelativeLayout mAdContainer;
//	DomobAdView mAdview320x50;
	
	@Override
	protected void onResume() {
		super.onResume();
		
//		Calendar now = Calendar.getInstance();
//		Context ctx = getApplicationContext();
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//		long lastClkAdTime = prefs.getLong(ConstDefinition.LAST_CLK_AD_TIME, 0);
//		if(now.getTimeInMillis() - lastClkAdTime < 86400 * 1000) {
//			mAdContainer.setVisibility(View.GONE);
//		}
//		else {
//			mAdContainer.setVisibility(View.VISIBLE);
//		}
//		showChapterContent(true);
		
		StatService.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(thread != null)
			thread.isRunning = false;
		
		StatService.onPause(this);
	}
	
	private void showChapterContent(boolean needLoad) {
		NovelDB db = new NovelDB(getApplicationContext());
		
		Cursor cursor = db.getRecordsBySQL("select * from book_chapter where content is not null and book_id=" + bookChapter.getBook().getPriId() + " order by orderNum desc");
		try {
			if(cursor.moveToFirst()) {
				ContentValues values = new ContentValues();
				values.put(NovelDB.SUBSCRIPTION_READ_TITLE, cursor.getString(2));
				values.put(NovelDB.SUBSCRIPTION_READ_URL, cursor.getString(3));
				db.update(NovelDB.SUBSCRIPTION_TABLE, values, "book_id=" + bookChapter.getBook().getPriId(), null);
			}
		} finally {
			cursor.close();
		}
		
		cursor = db.getRecordsBySQL("select * from book_chapter where _id=" + bookChapter.getPriId());
		try {
			if(cursor.moveToFirst()) {
				String chapterContent = cursor.getString(8);
				if(chapterContent != null) {
					lyLoading.setVisibility(View.GONE); 
					chapterContent = "<html><body bgcolor=\"#999999\" style=\"line-height:1.5\">" 
							+ chapterContent.replaceAll("style=\".*?\"", "")
							+ "<br/><br/><br/><br/><br/></body></html>";
					wvBookDisplayer.loadData(chapterContent, "text/html; charset=UTF-8", null);
				}
				else if(needLoad){
					lyLoading.setVisibility(View.VISIBLE); 
					thread = new ProgressThread();
					thread.start();
				}
			}
		} finally {
			cursor.close();
			db.close();
		}
	}
	
	private ProgressThread thread;
	public static final int START_CODE = 0;
	public static final int UPDATING_CODE = 1;
	public static final int FINISHED_CODE = 2;
	public static final int FAILED_CODE = 3;
	
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if(msg.what == START_CODE) {
//        		isRunning = true;
        	}
        	else if(msg.what == FINISHED_CODE) {
        		lyLoading.setVisibility(View.GONE);
        		thread = null;
        		
        		Bundle bundle = msg.getData();
        		int _id = bundle.getInt("id");
        		String content = bundle.getString("content");
        		NovelDB db = new NovelDB(getApplicationContext());
        		try {
        			db.updateChapterContent(content, _id);
        		} finally {
        			db.close();
        		}
        		
        		showChapterContent(false);
        	}
        	else if(msg.what == UPDATING_CODE) {
  //      		showChapterContent();
        	}
        	else if (msg.what == FAILED_CODE) {
        		lyLoading.setVisibility(View.GONE);
        		thread = null;
        		String chapterContent = "<html><body bgcolor=\"#999999\" style=\"line-height:1.5\">加载失败，请检查网络连接。您可以稍后再试，或者在订阅界面，长按小说名选择其他小说源。<br/><br/><br/><br/><br/></body></html>";
				wvBookDisplayer.loadData(chapterContent, "text/html; charset=UTF-8", null);
        	}
        }
    };

	class ProgressThread extends Thread
    {
    	public boolean isRunning;

        @Override
        public void run()
        {
        	String domain = bookChapter.getBook().getSrcEntity().getDomain();
        	Pattern pContent = Pattern.compile(bookChapter.getBook().getSrcEntity().getPatternContent());
        	Pattern pBackupContent = Pattern.compile("<div class=[\"']trans[\"']>[\\s\\S]*?</div>([\\s\\S]*?)<div class=\"footer\">");
        	String chapterUrl = bookChapter.getChapterUrl();
        	
        	String res = HtmlService.getHtmlByGet(domain + chapterUrl);
        	
        	int page = 1;
    		Pattern pPage = Pattern.compile("tc_next[\\s\\S]*?href[\\s\\S]*?=[\\s\\S]*?<span[\\s\\S]*?>(\\d+?)</span>/(\\d+?)[^\\d]");
    		Matcher m = pPage.matcher(res);
    		if(m.find()) {
    			try {
    				page = Integer.parseInt(m.group(2));
    			} catch(Exception e) {
    				page = 1;
    			}
    		}
        	
    		int i = 1;
    		StringBuilder sb = new StringBuilder(65536);
    		while(i <= page) {
	    		m = pContent.matcher(res);
	    		if(m.find()) {
	    			String content = m.group(1);
	    			for(String invalidContent: ConstDefinition.INVALID_CONTENT)
	    				content = content.replaceAll(invalidContent, "");
	    			sb.append(content);
	    		}
	    		else {
	    			Matcher m2 = pBackupContent.matcher(res);
	    			if(m2.find()) {
	    				String content = m2.group(1);
		    			for(String invalidContent: ConstDefinition.INVALID_CONTENT)
		    				content = content.replaceAll(invalidContent, "");
		    			sb.append(content);
	    			}
	    		}
	    		i ++;
	    		if(i > page)
	    			break;
	    		res = HtmlService.getHtmlByGet(domain + chapterUrl + "&pi=" + i);
    		}

    		if(sb.length() > 0) {
	    		Message msg = handler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putInt("id", bookChapter.getPriId());
				bundle.putString("content", sb.toString());
				msg.setData(bundle);
				msg.what = FINISHED_CODE;
				handler.sendMessage(msg);
    		}
    		else {
    			handler.sendEmptyMessage(FAILED_CODE);
    		}
    		
        	isRunning = false;
 //       	handler.sendEmptyMessage(FINISHED_CODE);
        }
    }
}
