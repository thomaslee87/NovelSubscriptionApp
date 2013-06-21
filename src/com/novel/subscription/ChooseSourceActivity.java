package com.novel.subscription;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

public class ChooseSourceActivity extends ListActivity {
	
	public class SourceSiteEntity {
		public String site;
		public String url;
		public String update;
		
		public SourceSiteEntity(String site, String url, String update) {
			this.site = site;
			this.url = url;
			this.update = update;
		}
	}
	
	private int TestNetworkStatus() {
		  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  if (ni == null)
			  return -1;
		  else {
			  return ni.getType();
		  }
	}
	private LinearLayout lyLoading;
	private SubscriptionEntity subscription;
	
	private SearchThread searchThread = null;
	
	private ArrayList<SourceSiteEntity> items = new ArrayList<SourceSiteEntity>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_source);
		lyLoading = (LinearLayout) findViewById(R.id.loading);
		lyLoading.setVisibility(View.VISIBLE);
		
		subscription = (SubscriptionEntity)this.getIntent().getSerializableExtra("subscription");
		
		searchThread = new SearchThread();
		searchThread.sKeyWord = subscription.getBook().getName();
		searchThread.isRunning = true;
		searchThread.start();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}
	private static final int SEARCH_SUCCESS_CODE = 100;
	private static final int SEARCH_FAIL_CODE = 200;
	
	private static final String BUNDLE_SEARCH_KEY = "keyword";
	private static final String BUNDLE_BOOK_SITE_KEY = "site";
	private static final String BUNDLE_BOOK_URL_KEY = "url";
	private static final String BUNDLE_BOOK_UPDATE_KEY = "update";
	
	Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if(msg.what == SEARCH_SUCCESS_CODE) {
        		if(searchThread == null)
        			return;
        		searchThread.isRunning = false;
        		searchThread = null;
        		
        		Bundle bundle = msg.getData();
        		
        		String[] bookSite = bundle.getString(BUNDLE_BOOK_SITE_KEY).split("\n");
        		String[] bookUrl = bundle.getString(BUNDLE_BOOK_URL_KEY).split("\n");
        		String[] bookUpdate = bundle.getString(BUNDLE_BOOK_UPDATE_KEY).split("\n");
        		
        		items.clear();
        		for(int i = 0; i < bookSite.length; i ++)
        			items.add(new SourceSiteEntity(bookSite[i], bookUrl[i], bookUpdate[i]));
        		
        		MoreSourceListViewAdapter adapter = new MoreSourceListViewAdapter(ChooseSourceActivity.this, items);
        		setListAdapter(adapter);
        		lyLoading.setVisibility(View.GONE);
        	}
        	else if(msg.what == SEARCH_FAIL_CODE) {
        		if(searchThread == null)
        			return;
        		searchThread.isRunning = false;
        		searchThread = null;
        		
        		Bundle bundle = msg.getData();
        		String keyWord = bundle.getString(BUNDLE_SEARCH_KEY);
        		
        		new AlertDialog.Builder(ChooseSourceActivity.this)
	        		.setTitle("���ҽ��")  
	        		.setMessage("�ܱ�Ǹ��δ���ҵ������Դ�� " + keyWord)  
	        		.setPositiveButton("ȷ��", null)  
	        		.show();
        	}
        }
    };
    
    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	
    	new AlertDialog.Builder(ChooseSourceActivity.this)
        .setTitle("����ѡ��Դ")
        .setIcon(R.drawable.ic_launcher)
        .setMessage("  �������л�С˵����Դ��\n��ע�⣺�˲����������ǰ������½���Ϣ��")
        .setPositiveButton("�ǵ�", new DialogInterface.OnClickListener() {
	 					
			public void onClick(DialogInterface dialog, int which) {
				if(items.get(position).site.equals(subscription.getBook().getSite())) {
					Toast.makeText(ChooseSourceActivity.this, "��Դ����ʹ���У������л���", Toast.LENGTH_SHORT).show();
				}
				else {
					NovelDB db = new NovelDB(getApplicationContext());
					db.beginTransaction();
					try {
						ContentValues values = new ContentValues();  
						
						values.put(NovelDB.SRC_ID, 1);
						values.put(NovelDB.BOOK_URL, ConstDefinition.MBAIDU_STRING + items.get(position).url);
						values.put(NovelDB.TYPE_ID, 0);
						values.put(NovelDB.BOOK_UPDATE_ORDER, 0);
						values.put(NovelDB.BOOK_SITE, items.get(position).site);
						db.update(NovelDB.BOOK_LIST_TABLE, values, "_id = " + subscription.getBook().getPriId(), null);
						
						db.delete(NovelDB.BOOK_CHAPTER_TABLE, "book_id = " + subscription.getBookId(), null);
						
						values.clear();
						values.put(NovelDB.SUBSCRIPTION_LATEST_TITLE, "");
						values.put(NovelDB.SUBSCRIPTION_LATEST_URL, "");
						values.put(NovelDB.SUBSCRIPTION_READ_TITLE, "");
						values.put(NovelDB.SUBSCRIPTION_READ_URL, "");
						db.update(NovelDB.SUBSCRIPTION_TABLE, values, "_id = " + subscription.getId(), null);
						db.setTransactionSuccessful();
					} finally {
						db.endTransaction();
						db.close();
					}
					Toast.makeText(ChooseSourceActivity.this, "�л���ϣ��������¼����½��б�", Toast.LENGTH_SHORT).show();
					
					Intent data = new Intent();  
		            data.putExtra("book_id", subscription.getBookId());  
		            setResult(20, data);  
		            
					finish();
				}
			}
			
		})
	 	.setNegativeButton("ȡ��", null)
	    .show();
    	
    }
    
    class SearchThread extends Thread {
    	public boolean isRunning = false;
    	public String sKeyWord;
    	
    	@Override
    	public void run() {
    		String htmlContent = "";
			try {
				htmlContent = HtmlService.getHtmlByGet(ConstDefinition.MBAIDU_QUERY + URLEncoder.encode(sKeyWord, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				Log.i("Exception", e.toString());
			}
    		Pattern p = Pattern.compile(ConstDefinition.PATTERN_QUERY);
    		Matcher m = p.matcher(htmlContent);
    		if(m.find()) {
    			String novelBody = m.group(1);
    			Pattern pp = Pattern.compile("class=\"site\">[\\s\\S]*?href=[\"']\\./(.*?)[\"']");
    			Matcher mm = pp.matcher(novelBody);
    			String moreUrl = null;
    			if(mm.find()) {
    				moreUrl = mm.group(1).replaceAll("&amp;", "&");
    				String moreHtmlContent = HtmlService.getHtmlByGet(ConstDefinition.MBAIDU_STRING + moreUrl); 
    				
    				Pattern ppp = Pattern.compile("class=\"result\">[\\s\\S]*?class=\"site\">(.*?)</span>.*?href\\s*=\\s*[\"']\\.*?(/.*?)[\"'].*?>(.*?)</a>");
    				Matcher mmm = ppp.matcher(moreHtmlContent);
    				StringBuilder sbSite = new StringBuilder(4096);
    				StringBuilder sbUrl = new StringBuilder(4096);
    				StringBuilder sbUpdate = new StringBuilder(4096);
    				while(mmm.find()) {
    					String site = mmm.group(1);
    					String novelUrl = mmm.group(2);
    					String novelLatestUpdate = mmm.group(3);
    					
    					sbSite.append(site).append("\n");
    					sbUrl.append(novelUrl).append("\n");
    					sbUpdate.append(novelLatestUpdate).append("\n");
    					
    				}
    				
    				Bundle bundle = new Bundle();
					bundle.putString(BUNDLE_BOOK_SITE_KEY, sbSite.toString());
	    			bundle.putString(BUNDLE_BOOK_URL_KEY, sbUrl.toString().replaceAll("&amp;", "&"));
	    			bundle.putString(BUNDLE_BOOK_UPDATE_KEY, sbUpdate.toString());
	    			Message msg = handler.obtainMessage();
	    			msg.setData(bundle);
	    			msg.what = SEARCH_SUCCESS_CODE;
	    			handler.sendMessage(msg);
    			}
    		}
    		else {
    			Bundle bundle = new Bundle();
    			bundle.putString(BUNDLE_SEARCH_KEY, sKeyWord);
    			Message msg = handler.obtainMessage();
    			msg.setData(bundle);
    			msg.what = SEARCH_FAIL_CODE;
    			handler.sendMessage(msg);
    		}
    	}
    }
    
}
