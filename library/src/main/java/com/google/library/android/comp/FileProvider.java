package com.google.library.android.comp;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;

import java.io.File;

public class FileProvider extends androidx.core.content.FileProvider {
	private static final String AUTHOR = "com.google.library.fileProvider";
	
	public static Uri getUriForFile(@NonNull Context context, @NonNull File file) {
		Uri uri;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			uri = getUriForFile(context, AUTHOR, file);
		} else {
			uri = Uri.fromFile(file);
		}
		return uri;
	}
}