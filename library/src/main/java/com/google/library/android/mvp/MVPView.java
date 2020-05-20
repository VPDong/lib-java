package com.google.library.android.mvp;

import android.content.Context;

public interface MVPView {
	Context getAppContext();

	void showMsg(String msg);

	void showErr(String msg);
}
