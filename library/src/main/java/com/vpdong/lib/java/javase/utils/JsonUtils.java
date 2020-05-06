package com.vpdong.lib.java.javase.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;

public class JsonUtils {
	private JsonUtils() throws Exception {
		throw new IllegalAccessException("can not to init instance for json utils");
	}
	
	private static final Gson GSON = new Gson();
	
	public static String toJson(Object object, String value) {
		try {
			return GSON.toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
			return value;
		}
	}
	
	public static <T> T fromJson(File json, Class<T> clazz) {
		try {
			if (json == null || !json.exists() || !json.isFile()) return null;
			String content = FileUtils.getContent(json);
			return fromJson(content, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T fromJson(byte[] json, Class<T> clazz) {
		try {
			return fromJson(new String(json), clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return GSON.fromJson(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T fromJson(File json, TypeToken<?> typeToken) {
		try {
			if (json == null || !json.exists() || !json.isFile()) return null;
			String content = FileUtils.getContent(json);
			return fromJson(content, typeToken);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T fromJson(byte[] json, TypeToken<?> typeToken) {
		try {
			return fromJson(new String(json), typeToken);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T fromJson(String json, TypeToken<?> typeToken) {
		try {
			// Type type = new TypeToken<List<String>>() {};
			return GSON.fromJson(json, typeToken.getType());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
