package com.google.library.android.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class RefreshLayout extends SwipeRefreshLayout {
	public RefreshLayout(Context context) {
		this(context, null);
	}
	
	public RefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
}