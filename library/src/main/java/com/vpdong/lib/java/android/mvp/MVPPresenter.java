package com.vpdong.lib.java.android.mvp;

import com.vpdong.lib.java.android.rxlib.RxLifeEvent;
import com.vpdong.lib.java.android.rxlib.RxLifeProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class MVPPresenter<V extends MVPView> {
	public static MVPPresenter newPresenter(Class<? extends MVPView> clazz) {
		try {
			Type clazzSuper = clazz.getGenericSuperclass();
			if (clazzSuper == null) return null;
			Class<?> clazzGene = (Class<?>) ((ParameterizedType) clazzSuper).getActualTypeArguments()[0];
			if (clazzGene == null) return null;
			return (MVPPresenter) clazzGene.newInstance();
		} catch (Exception ignored) {
			return null;
		}
	}
	
	private RxLifeProvider<RxLifeEvent> mLife;
	private V mView;
	
	protected MVPPresenter() {
	}
	
	public void onAttach(RxLifeProvider<RxLifeEvent> life, V view) {
		mLife = life;
		mView = view;
	}
	
	public void onDetach() {
		mView = null;
		mLife = null;
	}
	
	public RxLifeProvider<RxLifeEvent> getLife() {
		return mLife;
	}
	
	protected V getView() {
		return mView;
	}
	
	protected <M extends MVPModel> M getModel(Class<M> clazz) {
		return MVPModel.getModel(getView().getAppContext(), clazz);
	}
}
