package com.novel.subscription;

import java.io.Serializable;

import android.database.Cursor;

public class AppOptions implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int soundNotify;
	private int updatePeriod;
	private int autoDownloadRecentChapter;
	private int maxSubscription;
	private int silentNight;
	
	public AppOptions(Cursor cursor) {
		setSoundNotify(cursor.getInt(1));
		setUpdatePeriod(cursor.getInt(2));
		setAutoDownloadRecentChapter(cursor.getInt(3));
		setMaxSubscription(cursor.getInt(4));
		setSilentNight(cursor.getInt(5));
	}
	
	public int getSoundNotify() {
		return soundNotify;
	}
	public void setSoundNotify(int soundNotify) {
		this.soundNotify = soundNotify;
	}
	public int getUpdatePeriod() {
		return updatePeriod;
	}
	public void setUpdatePeriod(int udpatePeriod) {
		this.updatePeriod = udpatePeriod;
	}
	public int getAutoDownloadRecentChapter() {
		return autoDownloadRecentChapter;
	}
	public void setAutoDownloadRecentChapter(int autoDownloadRecentChapter) {
		this.autoDownloadRecentChapter = autoDownloadRecentChapter;
	}
	public int getMaxSubscription() {
		return maxSubscription;
	}
	public void setMaxSubscription(int maxSubscription) {
		this.maxSubscription = maxSubscription;
	}
	public int getSilentNight() {
		return silentNight;
	}
	public void setSilentNight(int silentNight) {
		this.silentNight = silentNight;
	}
	
	
}
