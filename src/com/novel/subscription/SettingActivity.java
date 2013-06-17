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
		
		Context c = getApplicationContext();
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
		updateInterval.setSummary(intervalString + "Сʱ");
		updateInterval.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				updateInterval.setSummary(newValue + "Сʱ");
				return true;
			}
		});
		
	}
	
}
