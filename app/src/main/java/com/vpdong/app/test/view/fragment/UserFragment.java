package com.vpdong.app.test.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.library.android.mvp.view.MVPFragment;
import com.vpdong.app.test.R;

public class UserFragment extends MVPFragment<UserPresenter> {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_user);
	}
	
	@Override
	public void initView(View root) {
		// todo
	}
	
	@Override
	public View getTabView(Context context) {
		if (mTabView == null) {
			mTabView = LayoutInflater.from(context).inflate(R.layout.lib_item_tab, null);
			ImageView iv = mTabView.findViewById(R.id.iv_tab);
			iv.setImageResource(R.drawable.icn_tab_user_selector);
			TextView tv = mTabView.findViewById(R.id.tv_tab);
			tv.setText("我的");
		}
		return mTabView;
	}
}
