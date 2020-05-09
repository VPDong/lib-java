package com.google.library.javase.utils;


import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Type;

public class JsonUtils {
	private static volatile Gson GSON = new Gson();
	
	private JsonUtils() throws Exception {
		throw new IllegalAccessException("can not to init instance for json utils");
	}
	
	public static String toJson(Object object, String value) {
		try {
			return GSON.toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
			return value;
		}
	}
	
	public static <T> T fromJson(File json, Type resp) {
		try {
			if (json == null || !json.exists() || !json.isFile()) return null;
			String content = FileUtils.getContent(json);
			return fromJson(content, resp);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T fromJson(byte[] json, Type resp) {
		try {
			return fromJson(new String(json), resp);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T fromJson(String json, Type resp) {
		try {
			if (resp instanceof Class) {
				return GSON.fromJson(json, resp);
			} else if (resp != null) {
				// Type type = new TypeToken<List<String>>() {}.getType();
				return GSON.fromJson(json, resp);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
