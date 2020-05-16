package com.google.library.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class RefreshLayout extends SwipeRefreshLayout {
	private boolean mPulling;
	private boolean mPushing;
	
	private View mHerderView;
	private View mFooterView;
	
	private OnPullListener mOnPullListener;
	private OnPushListener mOnPushListener;
	
	public RefreshLayout(Context context) {
		this(context, null);
	}
	
	public RefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setPulling(boolean pulling) {
		mPulling = pulling;
		setRefreshing(pulling);
	}
	
	public void setPushing(boolean pushing) {
		mPushing = pushing;
	}
	
	public void setHerderView(View herderView) {
		mHerderView = herderView;
	}
	
	public void setFooterView(View footerView) {
		mFooterView = footerView;
	}
	
	public void setOnPullListener(OnPullListener listener) {
		mOnPullListener = listener;
		setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (mOnPullListener != null) {
					mOnPullListener.onPull();
				}
			}
		});
	}
	
	public void setOnPushListener(OnPushListener listener) {
		mOnPushListener = listener;
	}
	
	public interface OnPullListener {
		void onPull();
	}
	
	public interface OnPushListener {
		void onPush();
	}
}