package com.vpdong.lib.java.android.mvp;

import android.content.Context;
import android.os.Handler;

public interface MVPView {
	Context getAppContext();

	Handler getHandler();

	void showMsg(String msg);

	void showErr(String msg);
}
