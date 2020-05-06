package com.vpdong.lib.test.view;

import android.os.Bundle;
import com.vpdong.lib.java.android.mvp.view.MVPActivity;
import com.vpdong.lib.test.R;

public class MainActivity extends MVPActivity<MainPresenter> {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
