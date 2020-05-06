package com.vpdong.lib.java.android.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vpdong.lib.java.android.mvp.MVPPresenter;
import com.vpdong.lib.java.android.mvp.MVPView;
import com.vpdong.lib.java.android.rxlib.RxLife;
import com.vpdong.lib.java.android.rxlib.RxLifeEvent;
import com.vpdong.lib.java.android.rxlib.RxLifeProvider;
import com.vpdong.lib.java.android.rxlib.RxLifeTransformer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static android.os.Looper.getMainLooper;

public abstract class MVPFragment<P extends MVPPresenter> extends Fragment
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@SuppressWarnings("unchecked")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		mPresenter = (P) MVPPresenter.newPresenter(getClass());
		if (mPresenter != null) {
			mPresenter.onAttach(this, this);
		}
		mHandler = new Handler(getMainLooper());
		mLifecycleSubject.onNext(RxLifeEvent.CREATE);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		mLifecycleSubject.onNext(RxLifeEvent.START);
	}

	@Override
	public void onResume() {
		super.onResume();
		mLifecycleSubject.onNext(RxLifeEvent.RESUME);
	}

	@Override
	public void onPause() {
		mLifecycleSubject.onNext(RxLifeEvent.PAUSE);
		super.onPause();
	}

	@Override
	public void onStop() {
		mLifecycleSubject.onNext(RxLifeEvent.STOP);
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		mLifecycleSubject.onNext(RxLifeEvent.DESTROY);
		if (mPresenter != null) {
			mPresenter.onDetach();
			mPresenter = null;
		}
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Context getAppContext() {
		Context context = getContext();
		return context == null ? null : context.getApplicationContext();
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
		// ExtUtils.Toast.show(getContext(), msg);
	}

	@Override
	public void showErr(String msg) {
		showMsg(msg);
	}
}
