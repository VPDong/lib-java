package com.vpdong.app.test.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.library.android.mvp.view.MVPActivity;
import com.vpdong.app.test.R;

public class MainActivity extends MVPActivity<MainPresenter> {
	public static void start(Context context) {
		if (context == null) return;
		Intent intent = new Intent(context, MainActivity.class);
		if (!(context instanceof Activity)) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public void initView() {
		getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				HomeActivity.start(MainActivity.this);
				finish();
			}
		}, 2000);
	}
}
