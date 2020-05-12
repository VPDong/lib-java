package com.vpdong.app.test.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.library.android.mvp.view.MVPFragment;
import com.vpdong.app.test.R;

public class VideoFragment extends MVPFragment<VideoPresenter> {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_video);
	}
	
	@Override
	public void initView(View root) {
		// todo
	}
	
	@Override
	public View getTabView(Context context) {
		if (mTabView == null) {
			mTabView = LayoutInflater.from(context).inflate(R.layout.item_tab, null);
			ImageView iv = mTabView.findViewById(R.id.iv_tab);
			iv.setImageResource(R.drawable.icn_tab_video_selector);
			TextView tv = mTabView.findViewById(R.id.tv_tab);
			tv.setText("视频");
		}
		return mTabView;
	}
}
