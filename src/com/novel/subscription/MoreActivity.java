package com.novel.subscription;

import com.novel.subscription.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MoreActivity extends Activity {
	
	private WebView aboutDisplayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		aboutDisplayer = (WebView) findViewById(R.id.aboutDisplayer);
		aboutDisplayer.loadUrl("file:///android_asset/about.html");

	}

}
