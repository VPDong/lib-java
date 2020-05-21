package com.google.library.android.comp;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;

import java.io.File;

public class FileProvider extends androidx.core.content.FileProvider {
	public static Uri getUriForFile(@NonNull Context context, @NonNull File file) {
		final String author = String.format("%s.fileprovider", context.getPackageName());
		Uri uri;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			uri = getUriForFile(context, author, file);
		} else {
			uri = Uri.fromFile(file);
		}
		return uri;
	}
}