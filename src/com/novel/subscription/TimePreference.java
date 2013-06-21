package com.novel.subscription;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {
	private int hour = 0;
	private int minute = 0;
	private TimePicker picker = null;

	private String currentValue = null;

	public TimePreference(Context ctxt, AttributeSet attrs) {
		super(ctxt, attrs);

		setPositiveButtonText("确定");
		setNegativeButtonText("取消");

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctxt);
		if (getKey().equals("timePickerUpdateStart")) {
			currentValue = sp.getString("timePickerUpdateStart", "07:00");
		} else if (getKey().equals("timePickerUpdateEnd")) {
			currentValue = sp.getString("timePickerUpdateEnd", "23:00");
		} else {
			currentValue = null;
		}
	}

	@Override
	protected View onCreateDialogView() {
		picker = new TimePicker(getContext());
		picker.setIs24HourView(true);

		return (picker);
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);

		picker.setCurrentHour(hour);
		picker.setCurrentMinute(minute);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			hour = picker.getCurrentHour();
			String hourString = hour + "";
			if (hour < 10)
				hourString = "0" + hour;
			minute = picker.getCurrentMinute();
			String minString = minute + "";
			if (minute < 10)
				minString = "0" + minute;

			String time = hourString + ":" + minString;

			if (callChangeListener(time)) {
				persistString(time);
			}
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return (a.getString(index));
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		String[] timeItems = new String[] { "0", "0" };
		if (currentValue != null)
			timeItems = currentValue.split(":");
		hour = Integer.parseInt(timeItems[0]);
		minute = Integer.parseInt(timeItems[1]);
	}
}
