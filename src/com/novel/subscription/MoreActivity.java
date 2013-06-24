package com.novel.subscription;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.baidu.mobstat.StatService;

public class MoreActivity extends Activity {
	
	private WebView aboutDisplayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		aboutDisplayer = (WebView) findViewById(R.id.aboutDisplayer);
		aboutDisplayer.loadUrl("file:///android_asset/about.html");

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
