package com.example.novel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BookChapterActivity extends Activity {
	
	private static final int SHOW_CHAPTER_COUNT = 50;
	private static final int UPDATE_LIST_COUNT = 50000000;
	
	private int currentPage = 0;
	private int totalPages = 0;
	private boolean isLastPage = false;
	private BookEntity book;
	private ListView lvChapterList;
	private ArrayList<BookChapterEntity> items = new ArrayList<BookChapterEntity>();
	private ArrayList<BookChapterEntity> updateItems = new ArrayList<BookChapterEntity>();
	
	private TextView tvPrompt;
	private TextView tvRefresh, tvNet ;
	private ProgressBar pbCircle;
	private ProgressThread pgThread;
	private Button bnPrevPage, bnNextPage;
	private ListView lvUpdateList;
	
	private int TestNetworkStatus() {
		  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  if (ni == null)
			  return -1;
		  else {
			  return ni.getType();
		  }
	}
	
	private void loadChapterInfo() {
		
		if(TestNetworkStatus() == -1) {
			tvNet.setVisibility(View.VISIBLE);
			tvRefresh.setVisibility(View.GONE);
			pbCircle.setVisibility(View.GONE);
			return;
		}
		else {
			tvNet.setVisibility(View.GONE);
		}
		
		if(pgThread == null) {
			pbCircle.setVisibility(View.VISIBLE);
			tvRefresh.setVisibility(View.VISIBLE);
			
			bnPrevPage.setEnabled(false);
			bnNextPage.setEnabled(false);
			
			pgThread = new ProgressThread();
			pgThread.start();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_chapter_overview);
		
		lvChapterList = (ListView)findViewById(R.id.chapter_list);
		lvChapterList.setCacheColorHint(Color.TRANSPARENT);
		
		tvRefresh = (TextView) findViewById(R.id.tvRefresh);
		tvNet = (TextView) findViewById(R.id.tvNet);
		tvPrompt = (TextView) findViewById(R.id.udPrompt);
		pbCircle = (ProgressBar) findViewById(R.id.circlie_progressbar);
		pbCircle.setIndeterminate(false);
		lvUpdateList = (ListView) findViewById(R.id.update_list);
		
		bnPrevPage = (Button) findViewById(R.id.pPage);
		bnNextPage = (Button) findViewById(R.id.nPage);
		
		lvChapterList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				
				Intent intent = new Intent(BookChapterActivity.this, BookReaderActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("bookChapter", ((BookChapterEntity)adapter.getItemAtPosition(pos)));
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});
		
		pgThread = null;
		
		book = (BookEntity)this.getIntent().getSerializableExtra("book");

		bnPrevPage.setEnabled(false);
		bnNextPage.setEnabled(false);
		
		bnPrevPage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentPage > 0) {
					currentPage --;
					showChapterList(book);
				}
			}
			
		});
		
		bnNextPage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isLastPage) {
					currentPage ++;
					showChapterList(book);
				}
			}
			
		});
		
		showChapterList(book);
			
//		loadChapterInfo();
	}
	
	private void showChapterList(BookEntity book) {
		items.clear();
		NovelDB db = new NovelDB(getApplicationContext());
		Cursor cursor = db.getRecordsBySQL("select * from book_chapter where book_id=" + book.getPriId() + " order by orderNum desc");
		int i = 0;
		try {
			int totalRecords = cursor.getCount();
			totalPages = totalRecords / SHOW_CHAPTER_COUNT + (totalRecords % SHOW_CHAPTER_COUNT == 0 ? 0 : 1);
			if(totalPages == 0) {
				totalPages = 1;
				isLastPage = true;
			}
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					if(i >= currentPage * SHOW_CHAPTER_COUNT && i < (currentPage + 1) * SHOW_CHAPTER_COUNT) {
	//				"_id intger primary key autoincrement," +
	//						"book_id integer not null," +
	//						"title text not null," +
	//						"url text not null," +
	//						"isRead integer default 0 not null," +
	//						"isDownload integer default 0 not null," + 
	//						"updateFlag integer default 0 not null," +
	//						"content text" +
						int _id = cursor.getInt(0);
						String chapterTitle = cursor.getString(2);
						String chapterUrl = cursor.getString(3);
						int isRead = cursor.getInt(4);
						int isDownload = cursor.getInt(5);
						int updateFlag = cursor.getInt(6);
						int order = cursor.getInt(7);
						int isUpdate = cursor.getInt(9);
						
						items.add(new BookChapterEntity(_id, chapterTitle, chapterUrl, isRead, isDownload, updateFlag, order, isUpdate, book));
						
						if(i == 0) {
		//					db.executeSQL("update ")
							ContentValues values = new ContentValues();
							values.put(NovelDB.SUBSCRIPTION_LATEST_TITLE, chapterTitle);
							db.update(NovelDB.SUBSCRIPTION_TABLE, values, "book_id = " + book.getPriId(), null);
						}
					}
					else if(i >= (currentPage + 1) * SHOW_CHAPTER_COUNT) {
						break;
					}
						
					i ++;
					cursor.moveToNext();
				}
				if(cursor.isAfterLast()) 
					isLastPage = true;
				else
					isLastPage = false;
			}
		} finally {
			cursor.close();
			db.close();
		}

		BookChapterListViewAdapter adapter = new BookChapterListViewAdapter(this, items);
		lvChapterList.setAdapter(adapter);
		
		bnPrevPage.setEnabled(true);
		bnNextPage.setEnabled(true);
		if(currentPage == 0)
			bnPrevPage.setEnabled(false);
		if(isLastPage)
			bnNextPage.setEnabled(false);
		tvPrompt.setText("最近的更新：（第" + (currentPage + 1) + "页/共" + totalPages + "页）");
	}
	
	private void showUpdateChapterList(BookEntity book) {
		updateItems.clear();
		NovelDB db = new NovelDB(getApplicationContext());
		db.beginTransaction();
		Cursor cursor = db.getRecordsBySQL("select * from book_chapter where book_id=" + book.getPriId() + " and isUpdate=1 order by orderNum desc");
		int i = 0;
		try {
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					int _id = cursor.getInt(0);
					String chapterTitle = cursor.getString(2);
					String chapterUrl = cursor.getString(3);
					int isRead = cursor.getInt(4);
					int isDownload = cursor.getInt(5);
					int updateFlag = cursor.getInt(6);
					int order = cursor.getInt(7);
					int isUpdate = cursor.getInt(9);
					
					updateItems.add(new BookChapterEntity(_id, chapterTitle, chapterUrl, isRead, isDownload, updateFlag, order, isUpdate, book));
					
					ContentValues values = new ContentValues();
					values.put(NovelDB.CHAPTER_IS_UPDATE, 0);
					db.update(NovelDB.BOOK_CHAPTER_TABLE, values, "_id=" + _id, null);
					
					if(i == 0) {
						values = new ContentValues();
						values.put(NovelDB.SUBSCRIPTION_LATEST_TITLE, chapterTitle);
						db.update(NovelDB.SUBSCRIPTION_TABLE, values, "book_id = " + book.getPriId(), null);
					}
					
					cursor.moveToNext();
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			cursor.close();
			db.close();
		}

		BookChapterListViewAdapter adapter = new BookChapterListViewAdapter(this, updateItems);
		lvUpdateList.setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		showChapterList(book);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(pgThread != null)
			pgThread.isRunning = false;
	}
	
	public static final int START_CODE = 0;
	public static final int UPDATING_CODE = 1;
	public static final int FINISHED_CODE = 2;
	
	Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if(msg.what == FINISHED_CODE) {
        		pbCircle.setVisibility(View.GONE);
        		tvRefresh.setVisibility(View.GONE);
        		pgThread = null;
        		showChapterList(book);
//        		showUpdateChapterList(book);
        	}
//        	else if(msg.what == UPDATING_CODE) {
//        		Bundle bundle = msg.getData();
//        		int id = bundle.getInt("id");
//        		String title = bundle.getString("title");
//        		NovelDB db = new NovelDB(getApplicationContext());
//        		try {
//        			ContentValues values = new ContentValues();
//        			values.put(NovelDB.SUBSCRIPTION_LATEST_TITLE, title);
//        			db.update(NovelDB.SUBSCRIPTION_TABLE, values, "_id=" + id, null);
////        			db.executeSQL("update subscription set latest_title = '" + title + "' where _id = '" + id + "'");
//        		} finally {
//        			db.close();
//        		
//        		}
//        		showChapterList(book);
//        	}
        }
    };
    
    class ProgressThread extends Thread
    {
    	public boolean isRunning;
    	public boolean firstStartup;

        private NovelDB db;
        
		public String getHtmlByGet(String _url){
			String result = "";
			HttpClient client = new DefaultHttpClient();
	        try {
	            HttpGet get = new HttpGet(_url);
	            get.setHeader("Accept-Charset", "GBK"); 
	            HttpResponse response = client.execute(get); 
	            HttpEntity resEntity = response.getEntity();
	            if (resEntity != null) {    
	                result = EntityUtils.toString(resEntity,"GBK");
	            }
	        } catch (Exception e) {
	        	Log.i("ReadList", "In getHtmlByGet: " + e.toString());
	            e.printStackTrace();
	        } finally {
	        	client.getConnectionManager().shutdown();
	        }
	        return result;
	    }

        @Override
        public void run()
        {
        	isRunning = true;
        	db = new NovelDB(getApplicationContext());
        	
    		String res = getHtmlByGet(book.getSrcEntity().getDomain() + book.getUrl());
        	
        	Pattern pChapter = Pattern.compile(book.getSrcEntity().getPatternChapter());
        	Pattern pBody = Pattern.compile(book.getSrcEntity().getPatternBody());
        	Pattern pOrder = Pattern.compile(book.getSrcEntity().getPatternOrder());
        	
    		Queue<BookChapterEntity> chapterQueue = new LinkedList<BookChapterEntity>();
    		
    		Matcher mBody = pBody.matcher(res);
    		if(mBody.find()) {
    			String body = mBody.group(1);
        		Matcher m = pChapter.matcher(body);
        		while(m.find() && isRunning) {
        			if(chapterQueue.size() >= UPDATE_LIST_COUNT)
        				chapterQueue.remove();
        			String url = m.group(1);
        			String title = m.group(2);
        			Matcher mUrlOrder = pOrder.matcher(url);
        			if(mUrlOrder.find()) {
        				int chapterOrder = Integer.parseInt(mUrlOrder.group(1));
        				if(chapterOrder > book.getUpdateOrder())
        					chapterQueue.add(new BookChapterEntity(-1, title, url, 0, 0, 0, chapterOrder, 1, book));
        			}
        		}
    		}
    		db.beginTransaction();
    		try {
	    		while(!chapterQueue.isEmpty()) {
	    			db.addChapter(chapterQueue.remove());
	    		}
	    		db.setTransactionSuccessful();
    		} finally {
	    		db.endTransaction();
	    		db.close();
    		}
    		
        	isRunning = false;
        	handler.sendEmptyMessage(FINISHED_CODE);
        }
    }

}
