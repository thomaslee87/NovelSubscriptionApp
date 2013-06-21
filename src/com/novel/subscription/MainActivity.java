package com.novel.subscription;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;

import net.youmi.android.AdManager;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActivityGroup;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;

import com.baidu.mobstat.StatService;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {
	
	public static final String PUBLISHER_ID = "56OJzYT4uNQKns3xwL";
	public static final String InlinePPID = "16TLm_IlApcTwNUHb4Wb5Hwk";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);
		
		AdManager.getInstance(this).init("a0659380b6ce6b1b",
				"e9558d8cbbc1c11f", false);
		
		StatService.setSessionTimeOut(30);

		final AppOptions appOptions = null;// new AppOptions(null);
		
//		Button btnStart = (Button) findViewById(R.id.btnStart);
//		btnStart.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(MainActivity.this, UpdateService.class);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("option", appOptions);
//				intent.putExtras(bundle);
//                startService(intent); //开始服务
//			}
//		});
//		Button btnStop = (Button) findViewById(R.id.btnStop);
//		btnStop.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(MainActivity.this, UpdateService.class);
//	                stopService(intent);
//	            }
//		});
		
		
		Context ctx = MainActivity.this;       
		SharedPreferences config = ctx.getSharedPreferences("Config", MODE_PRIVATE);
		int version = config.getInt(ConstDefinition.KEY_VERSION, -1);
//		if(ConstDefinition.CURRENT_VERSION > version) {
			
	        HashSet<String> srcDomainSet = new HashSet<String>();
	        HashSet<String> bookTypeSet = new HashSet<String>();
	        HashSet<String> curBookSet = new HashSet<String>();
			
			NovelDB db = new NovelDB(getApplicationContext());
			Cursor cursor = db.getRecordsBySQL("select * from source");
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					srcDomainSet.add(cursor.getString(2));
					cursor.moveToNext();
				}
			}
			
			cursor = db.getRecordsBySQL("select * from book_type");
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					bookTypeSet.add(cursor.getString(2));
					cursor.moveToNext();
				}
			}
			
			cursor = db.getRecordsBySQL("select * from book_list");
			if(cursor.moveToFirst()) {
				while(!cursor.isAfterLast()) {
					curBookSet.add(cursor.getString(3));
					cursor.moveToNext();
				}
			}
			
			db.beginTransaction();
			try {
				InputStream in = getResources().getAssets().open("default.conf");
				int length = in.available();         
				byte [] buffer = new byte[length];        
				in.read(buffer);            
				String res = EncodingUtils.getString(buffer, "UTF-8");     
				
				JSONObject jsonObj = new JSONObject(res);
				JSONArray source = jsonObj.getJSONArray(ConstDefinition.JSON_SOURCE_KEY);
		   		for(int i = 0; i < source.length(); i ++) {
		   			JSONObject c = source.getJSONObject(i);
		   			if(!srcDomainSet.contains(c.getString("name"))) {
		   				db.createSourceRecords(c);
		   				srcDomainSet.add(c.getString("name"));
		   			}
		   			else {
		   				db.updateSourceRecords(c);
		   			}
		   		}
		   		
		   		JSONObject jsonObjType = jsonObj.getJSONObject(ConstDefinition.JSON_TYPE_KEY);
		   		Iterator<String> iter = jsonObjType.keys();
		   		while(iter.hasNext()) {
		   			String key = iter.next();
		   			if(!bookTypeSet.contains(key)) {
		   				db.createTypeRecords(Integer.parseInt(key), jsonObjType.getString(key));
		   				bookTypeSet.add(key);
		   			}
		   		}
		   		
		   		JSONArray list = jsonObj.getJSONArray(ConstDefinition.JSON_LIST_KEY);
		   		for(int i = 0; i < list.length(); i++) {
		   			JSONObject c = list.getJSONObject(i);
//		   			Log.i("debug", c.getString("name"));
		   			if(!curBookSet.contains(c.getString("name"))) {
		   				curBookSet.add(c.getString("name"));
		   				db.createBookRecords(c);
		   			}
		   			else {
		   				String updateSql = "update book_list set recommend =" + c.getInt("recommend") + " where name = '" +  URLDecoder.decode(c.getString("name"), "utf-8") + "'";
		   				db.executeSQL(updateSql);
		   			}
		   		}
		   		db.setTransactionSuccessful();
		   		
		   		Editor editor = config.edit();
				editor.putInt(ConstDefinition.KEY_VERSION, ConstDefinition.CURRENT_VERSION);
				editor.commit();
			} catch (Exception e) {
				Log.i("debug", e.toString());
			} finally {
				db.endTransaction();
				db.close();
			}
//		}
		
		TabHost tablehost=(TabHost) findViewById(R.id.tabhost);
		tablehost.setup(getLocalActivityManager());
		
		tablehost.addTab(tablehost.newTabSpec("dy").setContent(new Intent(this,SubscriptionActivity.class)).setIndicator(LayoutInflater.from(this).inflate(R.layout.tab_view,null)));
//		tablehost.addTab(tablehost.newTabSpec("dz").setContent(new Intent(this,SelectBooksActivity.class)).setIndicator(LayoutInflater.from(this).inflate(R.layout.tab_view2,null)));
		tablehost.addTab(tablehost.newTabSpec("sz").setContent(new Intent(this,SettingActivity.class)).setIndicator(LayoutInflater.from(this).inflate(R.layout.tab_view3,null)));
		tablehost.addTab(tablehost.newTabSpec("gd").setContent(new Intent(this,MoreActivity.class)).setIndicator(LayoutInflater.from(this).inflate(R.layout.tab_view4,null)));
		
//		Context context = getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		String intervalString = prefs.getString("updateInterval", "2"); // unit : s ; 2 hours default
		
		int interval = 7200;
		try {
			interval = Integer.parseInt(intervalString) * 3600;
		} catch(Exception e) {
			interval = 7200;
		}
		Calendar now = Calendar.getInstance();
//		now.add(Calendar.SECOND, 10);
		AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(ctx, SchedulerEventReceiver.class); // explicit intent
		PendingIntent intentExecuted = PendingIntent.getBroadcast(ctx, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), interval * 1000, intentExecuted);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
