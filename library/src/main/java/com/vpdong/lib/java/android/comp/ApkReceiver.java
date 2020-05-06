package com.vpdong.lib.java.android.comp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ApkReceiver extends BroadcastReceiver {
	private static volatile Listener gListener;

	public static void setListener(Listener listener) {
		gListener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null || intent.getAction() == null) return;
		//接收安装广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			if (gListener != null) {
				gListener.onInstall(context.getApplicationContext(), intent);
			}
		}
		//接收卸载广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			if (gListener != null) {
				gListener.onUninstall(context.getApplicationContext(), intent);
			}
		}
	}

	public interface Listener {
		void onInstall(Context context, Intent intent);

		void onUninstall(Context context, Intent intent);
	}
}
