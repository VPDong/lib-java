package com.google.library.android.view.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * https://www.jianshu.com/p/b987fad8fcb4
 */
public abstract class BaseBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
	public BaseBehavior() {
	}
	
	public BaseBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 确定使用Behavior的View要依赖的View的类型
	 */
	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
		return super.layoutDependsOn(parent, child, dependency);
		// return dependency instanceof NestedScrollingChild;
	}
	
	/**
	 * 当被依赖的View状态改变时回调
	 */
	@Override
	public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
		return super.onDependentViewChanged(parent, child, dependency);
	}
	
	/**
	 * 当被依赖的View移除时回调
	 */
	@Override
	public void onDependentViewRemoved(CoordinatorLayout parent, V child, View dependency) {
		super.onDependentViewRemoved(parent, child, dependency);
	}
	
	/**
	 * 是否拦截触摸事件
	 */
	@Override
	public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent ev) {
		return super.onInterceptTouchEvent(parent, child, ev);
	}
	
	/**
	 * 处理触摸事件
	 */
	@Override
	public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent ev) {
		return super.onTouchEvent(parent, child, ev);
	}
	
	/**
	 * 测量使用Behavior的View尺寸
	 */
	@Override
	public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
		return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
	}
	
	/**
	 * 确定使用Behavior的View位置
	 */
	@Override
	public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
		return super.onLayoutChild(parent, child, layoutDirection);
	}
	
	/**
	 * 嵌套滑动开始(ACTION_DOWN)，确定Behavior是否要监听此次事件
	 */
	@Override
	public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V child, View directTargetChild, View target, int axes, int type) {
		return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
	}
	
	/**
	 * 嵌套滑动进行中，要监听的子View将要滑动，滑动事件即将被消费(但最终被谁消费，可以通过代码控制)
	 */
	@Override
	public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed, int type) {
		super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
	}
	
	/**
	 * 嵌套滑动进行中，要监听的子View的滑动事件已经被消费
	 */
	@Override
	public void onNestedScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
	}
	
	/**
	 * 要监听的子View即将快速滑动
	 */
	@Override
	public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY) {
		return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
	}
	
	/**
	 * 要监听的子View在快速滑动中
	 */
	@Override
	public boolean onNestedFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY, boolean consumed) {
		return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
	}
	
	/**
	 * 嵌套滑动结束(ACTION_UP或ACTION_CANCEL)
	 */
	@Override
	public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V child, View target, int type) {
		super.onStopNestedScroll(coordinatorLayout, child, target, type);
	}
}
