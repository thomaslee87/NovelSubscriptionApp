package com.novel.subscription;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NovelDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "app_novel";
	private static final int DATABASE_VERSION = 2;
	private static final String SOURCE_TABLE_CREATE = "create table source (" +
			"_id integer primary key autoincrement," +
			"id integer not null," +
			"name text not null," +
			"domain text not null," +
			"pattern_chapter text not null," +
			"pattern_img text not null," +
			"pattern_latest_title text not null," +
			"pattern_body text not null," +
			"pattern_order text not null," +
			"pattern_content text not null," +
			"pattern_page text not null" +
			");";
	
	private static final String BOOK_TYPE_TABLE_CREATE = "create table book_type (" +
			"_id integer primary key autoincrement," +
			"type_name text not null," +
			"type_id integer not null" +
			");";
	
	private static final String BOOK_LIST_TABLE_CREATE =  "create table book_list (" +
			"_id integer primary key autoincrement," +
			"src_id integer not null," +
			"type_id integer not null," +
			"name text not null," +
			"url text not null," +
			"author text default '' ," +
			"subscription integer default 0 not null," +
			"recommend integer default 0 not null," +
			"latest_title text default ''," +
			"updateOrder integer default 0 not null," + 
			"site text default '' " +
			");";
	
	private static final String SUBSCRIPTION_TABLE_CREATE = "create table subscription (" +
			"_id integer primary key autoincrement," +
			"book_id integer not null," +
			"latest_title text not null," +
			"latest_url text not null," +
			"read_title text not null," +
			"read_url text not null," +
			"valid integer not null," +
			"book_name text not null" +
			");";
	
	private static final String BOOK_CHAPTER_TABLE_CREATE = "create table book_chapter (" +
			"_id integer primary key autoincrement," +
			"book_id integer not null," +
			"title text not null," +
			"url text not null," +
			"isRead integer default 0 not null," +
			"isDownload integer default 0 not null," + 
			"updateFlag integer default 0 not null," +
			"orderNum integer default 0 not null," + 
			"content text," +
			"isUpdate integer default 0 not null" +
			");";
	
	private static final String APP_CONF_TABLE_CREATE = "create table app_options (" +
			"_id integer primary key autoincrement," +
			"soundNotify integer default 1 not null," +
			"updatePeriod integer default 3 not null," + 
			"autoDownloadRecentChapter integer default 0 not null, " + 	// when wifi
			"maxSubscription default 3 not null," +
			"silentNight default 1 not null" +
			");";
	
    public NovelDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(SOURCE_TABLE_CREATE);
		database.execSQL(BOOK_TYPE_TABLE_CREATE);
		database.execSQL(BOOK_LIST_TABLE_CREATE);
		database.execSQL(SUBSCRIPTION_TABLE_CREATE);
		database.execSQL(BOOK_CHAPTER_TABLE_CREATE);
		database.execSQL(APP_CONF_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(NovelDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS source");
        database.execSQL("DROP TABLE IF EXISTS book_type");
        database.execSQL("DROP TABLE IF EXISTS book_list");
        database.execSQL("DROP TABLE IF EXISTS subscription");
        database.execSQL("DROP TABLE IF EXISTS book_chapter");
        
        onCreate(database);
	}

}
