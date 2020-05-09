package com.google.library.android.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.library.android.mvp.MVPPresenter;
import com.google.library.android.mvp.MVPView;
import com.google.library.android.rxlib.RxLife;
import com.google.library.android.rxlib.RxLifeEvent;
import com.google.library.android.rxlib.RxLifeProvider;
import com.google.library.android.rxlib.RxLifeTransformer;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.LinkedList;
import java.util.List;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPresenter = (P) MVPPresenter.newPresenter(getClass());
		if (mPresenter != null) {
			mPresenter.onAttach(this, this);
		}
		mHandler = new Handler(getMainLooper());
		mLifecycleSubject.onNext(RxLifeEvent.CREATE);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initView();
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
	
	public abstract void initView();
	
	protected class PagerAdapter extends FragmentPagerAdapter {
		private final FragmentManager fragmentManager;
		private final List<MVPFragment> fragmentList;
		private final Context mContext;
		
		public PagerAdapter(Context context) {
			super(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
			fragmentManager = getSupportFragmentManager();
			fragmentList = new LinkedList<>();
			mContext = context.getApplicationContext();
		}
		
		public FragmentManager getManager() {
			return fragmentManager;
		}
		
		@Override
		public int getCount() {
			return fragmentList.size();
		}
		
		@Override
		public MVPFragment getItem(int position) {
			return fragmentList.get(position);
		}
		
		public void addItem(MVPFragment fragment) {
			if (fragment == null) return;
			fragmentList.add(fragment);
		}
		
		public void initTabView(TabLayout tabLayout) {
			if (tabLayout == null) return;
			for (int i = 0; i < fragmentList.size(); i++) {
				if (fragmentList.get(i).getTabView(mContext) != null
						&& tabLayout.getTabAt(i) != null) {
					tabLayout.getTabAt(i).setCustomView(fragmentList.get(i).getTabView(mContext));
				}
			}
		}
	}
}
