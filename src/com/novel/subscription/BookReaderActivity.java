package com.novel.subscription;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.novel.subscription.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		showChapterContent(true);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(thread != null)
			thread.isRunning = false;
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
					chapterContent = "<html><body bgcolor=\"#999999\" >" + chapterContent + "</body></html>";
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
    		
        	isRunning = false;
 //       	handler.sendEmptyMessage(FINISHED_CODE);
        }
    }
}
