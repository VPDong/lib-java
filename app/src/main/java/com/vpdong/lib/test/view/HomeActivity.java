package com.vpdong.lib.test.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.vpdong.lib.java.android.mvp.view.MVPActivity;
import com.vpdong.lib.test.R;

public class HomeActivity extends MVPActivity<HomePresenter> {
	public static void start(Context context) {
		if (context == null) return;
		Intent intent = new Intent(context, HomeActivity.class);
		if (!(context instanceof Activity)) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initView();
	}
	
	private void initView() {
		Toolbar toolbar = findViewById(R.id.toolbar);
		// toolbar.setNavigationIcon(R.drawable.back);
		setSupportActionBar(toolbar);
	}
}
