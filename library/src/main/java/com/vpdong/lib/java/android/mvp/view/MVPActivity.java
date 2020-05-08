package com.vpdong.lib.java.android.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.vpdong.lib.java.android.mvp.MVPPresenter;
import com.vpdong.lib.java.android.mvp.MVPView;
import com.vpdong.lib.java.android.rxlib.RxLife;
import com.vpdong.lib.java.android.rxlib.RxLifeEvent;
import com.vpdong.lib.java.android.rxlib.RxLifeProvider;
import com.vpdong.lib.java.android.rxlib.RxLifeTransformer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class MVPActivity<P extends MVPPresenter> extends AppCompatActivity
		implements MVPView, RxLifeProvider<RxLifeEvent> {
	private final BehaviorSubject<RxLifeEvent> mLifecycleSubject = BehaviorSubject.create();
	private P mPresenter;
	private Handler mHandler;
	
	@Override
	public final Observable<RxLifeEvent> life() {
		return mLifecycleSubject.hide();
	}
	
	@Override
	public final <T> RxLifeTransformer<T> bindToLife() {
		return RxLife.bind(mLifecycleSubject);
	}
	
	@Override
	public final <T> RxLifeTransformer<T> bindUntilEvent(RxLifeEvent event) {
		return RxLife.bindUntilEvent(mLifecycleSubject, event);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPresenter = (P) MVPPresenter.newPresenter(getClass());
		if (mPresenter != null) {
			mPresenter.onAttach(this, this);
		}
		mHandler = new Handler(getMainLooper());
		mLifecycleSubject.onNext(RxLifeEvent.CREATE);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mLifecycleSubject.onNext(RxLifeEvent.START);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mLifecycleSubject.onNext(RxLifeEvent.RESUME);
	}
	
	@Override
	protected void onPause() {
		mLifecycleSubject.onNext(RxLifeEvent.PAUSE);
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		mLifecycleSubject.onNext(RxLifeEvent.STOP);
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		mLifecycleSubject.onNext(RxLifeEvent.DESTROY);
		if (mPresenter != null) {
			mPresenter.onDetach();
			mPresenter = null;
		}
		super.onDestroy();
	}
	
	@Override
	public Context getAppContext() {
		return getApplicationContext();
	}
	
	public P getPresenter() {
		return mPresenter;
	}
	
	@Override
	public Handler getHandler() {
		return mHandler;
	}
	
	@Override
	public void showMsg(String msg) {
		// ExtUtils.Toast.show(getApplicationContext(), msg);
	}
	
	@Override
	public void showErr(String msg) {
		showMsg(msg);
	}
}
