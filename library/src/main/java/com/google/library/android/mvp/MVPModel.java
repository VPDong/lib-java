package com.google.library.android.mvp;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MVPModel {
	private static final Map<Class, MVPModel> MODELS = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <M extends MVPModel> M getModel(Context context, Class<M> clazz) {
		if (clazz == null) return null;
		if (!MODELS.containsKey(clazz)) {
			synchronized (MVPModel.class) {
				if (!MODELS.containsKey(clazz)) {
					try {
						Constructor constructor = clazz.getConstructor(Context.class);
						M model = (M) constructor.newInstance(context.getApplicationContext());
						MODELS.put(clazz, model);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (M) MODELS.get(clazz);
	}

	private Context mContext;

	protected MVPModel(Context context) {
		mContext = context;
	}

	protected Context getContext() {
		return mContext;
	}
}
