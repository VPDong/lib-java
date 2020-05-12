package com.google.library.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class LibViewPager extends ViewPager {
	private boolean isCanScroll = true;
	
	public LibViewPager(Context context) {
		super(context);
	}
	
	public LibViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isCanScroll) {
			return super.onTouchEvent(ev);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isCanScroll) {
			return super.onInterceptTouchEvent(ev);
		} else {
			return false;
		}
	}
	
	public void setCanScroll(boolean canScroll) {
		isCanScroll = canScroll;
	}
}
