package com.google.library.android.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class TaskUtils {
	static {
		HandlerThread bgThread = new HandlerThread("task-thread");
		M_BG_HANDLER = new Handler(bgThread.getLooper());
		M_UI_HANDLER = new Handler(Looper.getMainLooper());
	}
	
	private static volatile Handler M_BG_HANDLER;
	private static volatile Handler M_UI_HANDLER;
	
	public static void postBg(Runnable runnable) {
		if (runnable == null) return;
		M_BG_HANDLER.post(runnable);
	}
	
	public static void postUi(Runnable runnable) {
		if (runnable == null) return;
		M_UI_HANDLER.post(runnable);
	}
}
