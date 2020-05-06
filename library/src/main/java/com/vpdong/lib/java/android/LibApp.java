package com.vpdong.lib.java.android;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

public class LibApp extends Application {
	private static final Handler gHandler = new Handler();
	private static volatile Context gContext;
	
	@Override
	protected void attachBaseContext(Context context) {
		super.attachBaseContext(context);
		MultiDex.install(context);
		gContext = this;
	}
	
	public static Handler getHandler() {
		return gHandler;
	}
	
	public static Context getContext() {
		return gContext;
	}
}
