package com.vpdong.lib.java.javase.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class StrUtils {
	private StrUtils() throws Exception {
		throw new IllegalAccessException("can not to init instance for str utils");
	}
	
	public static String getSameStr(String str1, String str2) {
		if (str1 == null || str2 == null) return null;
		
		String maxStr = (str1.length() > str2.length()) ? str1 : str2;
		String minStr = (str1.length() > str2.length()) ? str2 : str1;
		
		String temp;
		for (int x = 0; x < minStr.length(); x++) {
			for (int y = 0, z = minStr.length() - x; z != minStr.length() + 1; y++, z++) {
				temp = minStr.substring(y, z);
				if (maxStr.contains(temp)) return temp;
			}
		}
		
		return null;
	}
	
	public static boolean isEmpty(Object... objs) {
		if (objs == null || objs.length == 0) return true;
		for (Object obj : objs) {
			if (obj == null) return true;
			if (obj instanceof File) {
				if (!((File) obj).exists()) {
					return true;
				}
			}
			if (obj instanceof String) {
				if (((String) obj).trim().equals("")
						|| ((String) obj).trim().equalsIgnoreCase("null")) {
					return true;
				}
			}
			if (obj instanceof Collection) {
				if (((Collection) obj).isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static SimpleDateFormat getTimeFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	}
	
	public static long getTimeMillis(String time) {
		try {
			return getTimeFormat().parse(time).getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static String getTimeString() {
		return getTimeFormat().format(new Date());
	}
	
	public static String getTimeString(long millis) {
		return getTimeFormat().format(millis);
	}
	
	public static String getTimeString(String millis) {
		try {
			return getTimeFormat().format(Long.valueOf(millis.trim()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
