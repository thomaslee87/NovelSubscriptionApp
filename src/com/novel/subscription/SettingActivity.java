package com.novel.subscription;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

public class SettingActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.settings);
		addPreferencesFromResource(R.layout.settings);
		final Preference updateNotify = findPreference("updateNotify");
		final Preference notifySound = findPreference("notifySound");
		final Preference notifyVibrate = findPreference("notifyVibrate");
		final ListPreference  updateInterval = (ListPreference)findPreference("updateInterval");
		
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean needUpdate = sp.getBoolean("updateNotify", false);
		notifySound.setEnabled(needUpdate);
		notifyVibrate.setEnabled(needUpdate);
		
		updateNotify.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
                boolean needUpdate = sp.getBoolean("updateNotify", false);
				notifySound.setEnabled(needUpdate);
				notifyVibrate.setEnabled(needUpdate);
				return true;
			}
		});
		
		String intervalString = sp.getString("updateInterval", "2");
		updateInterval.setSummary(intervalString + "小时");
		updateInterval.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				updateInterval.setSummary(newValue + "小时");
				return true;
			}
		});
		
		final TimePreference timePickerUpdateStart = (TimePreference) findPreference("timePickerUpdateStart");
		timePickerUpdateStart.setSummary(sp.getString("timePickerUpdateStart", "07:00"));
		timePickerUpdateStart.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if(((String)newValue).compareTo(sp.getString("timePickerUpdateEnd", "23:00")) >= 0) {
					Toast.makeText(SettingActivity.this, "更新结束时间必须大于开始时间。", Toast.LENGTH_SHORT).show();
					return false;
				}
				else {
					timePickerUpdateStart.setSummary((String)newValue);
					return true;
				}
			}
		});
		
		final TimePreference timePickerUpdateEnd = (TimePreference) findPreference("timePickerUpdateEnd");
		timePickerUpdateEnd.setSummary(sp.getString("timePickerUpdateEnd", "23:00"));
		timePickerUpdateEnd.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if(((String)newValue).compareTo(sp.getString("timePickerUpdateStart", "07:00")) <= 0) {
					Toast.makeText(SettingActivity.this, "更新结束时间必须大于开始时间。", Toast.LENGTH_SHORT).show();
					return false;
				}
				else {
					timePickerUpdateEnd.setSummary((String)newValue);
					return true;
				}
			}
		});
		
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
	
}
