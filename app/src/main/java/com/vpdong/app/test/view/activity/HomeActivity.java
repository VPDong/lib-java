package com.vpdong.app.test.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.library.android.mvp.view.MVPActivity;
import com.vpdong.app.test.R;
import com.vpdong.app.test.view.fragment.NewsFragment;
import com.vpdong.app.test.view.fragment.UserFragment;
import com.vpdong.app.test.view.fragment.VideoFragment;

public class HomeActivity extends MVPActivity<HomePresenter> {
	public static void start(Context context) {
		if (context == null) return;
		Intent intent = new Intent(context, HomeActivity.class);
		if (!(context instanceof Activity)) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}
	
	private PagerAdapter mPagerAdapter;
	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}
	
	@Override
	public void initView() {
		mPagerAdapter = new PagerAdapter(getAppContext());
		mPagerAdapter.addItem(new NewsFragment());
		mPagerAdapter.addItem(new VideoFragment());
		mPagerAdapter.addItem(new UserFragment());
		mViewPager = findViewById(R.id.viewPager);
		mViewPager.setAdapter(mPagerAdapter);
		mTabLayout = findViewById(R.id.tab_layout);
		mTabLayout.setupWithViewPager(mViewPager);
		mPagerAdapter.initTabView(mTabLayout);
	}
}
