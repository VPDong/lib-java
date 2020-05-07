package com.vpdong.lib.java.javase.utils;

import java.io.File;
import java.lang.reflect.Type;

public class JsonUtils {
	private static volatile Object GSON;
	
	static {
		GSON = RefUtils.newInstance("com.google.gson.Gson");
	}
	
	private JsonUtils() throws Exception {
		throw new IllegalAccessException("can not to init instance for json utils");
	}
	
	public static String toJson(Object object, String value) {
		try {
			// return GSON.toJson(object);
			return (String) RefUtils.invokeMethod(GSON, "toJson",
					new RefUtils.Param(Object.class, object));
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
	
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			// return GSON.fromJson(json, clazz);
			return (T) RefUtils.invokeMethod(GSON, "fromJson",
					new RefUtils.Param(String.class, json),
					new RefUtils.Param(clazz, clazz));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T fromJson(File json, Type type) {
		try {
			if (json == null || !json.exists() || !json.isFile()) return null;
			String content = FileUtils.getContent(json);
			return fromJson(content, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T fromJson(byte[] json, Type type) {
		try {
			return fromJson(new String(json), type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String json, Type type) {
		try {
			// Type type = new TypeToken<List<String>>() {}.getType;
			// return GSON.fromJson(json, type);
			return (T) RefUtils.invokeMethod(GSON, "fromJson",
					new RefUtils.Param(String.class, json),
					new RefUtils.Param(Type.class, type));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
