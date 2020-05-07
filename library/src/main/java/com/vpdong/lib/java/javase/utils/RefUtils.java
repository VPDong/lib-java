package com.vpdong.lib.java.javase.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class RefUtils {
	private RefUtils() throws Exception {
		throw new IllegalAccessException("can not to init instance for ref utils");
	}
	
	public static Object newInstance(String classStr, Param... params) {
		try {
			Class<?> clazz = Class.forName(classStr);
			if (params == null || params.length == 0) {
				return clazz.newInstance();
			} else {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Param.getTypes(params));
				constructor.setAccessible(true);
				return constructor.newInstance(Param.getValues(params));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isStaticFinal(Method method) {
		return (Modifier.isStatic(method.getModifiers())
				&& Modifier.isFinal(method.getModifiers()))
				|| method.isSynthetic();
	}
	
	public static boolean isStaticFinal(Field field) {
		return (Modifier.isStatic(field.getModifiers())
				&& Modifier.isFinal(field.getModifiers()))
				|| field.isSynthetic();
	}
	
	public static Object invokeMethod(Object object, String methodName, Param... params) {
		if (object == null || methodName == null) return null;
		try {
			for (Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
				Method method = null;
				try {// getDeclaredMethod获取的是类自身声明的所有方法，包含public、protected和private
					method = clazz.getDeclaredMethod(methodName, Param.getTypes(params));
				} catch (NoSuchMethodException ignored) {
					// ignored
				}
				if (method == null) continue;
				return method.invoke(object, Param.getValues(params));
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object getFieldValue(Object object, String fieldName) {
		if (object == null || fieldName == null) return null;
		try {
			for (Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
				// getDeclaredMethod获取的是类自身声明的所有变量，包含public、protected和private
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					if (field == null || !field.getName().equals(fieldName)) continue;
					field.setAccessible(true);
					return field.get(object);
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean setFieldValue(Object object, String fieldName, Object filedValue) {
		if (object == null || fieldName == null) return false;
		try {
			for (Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
				// getDeclaredMethod获取的是类自身声明的所有变量，包含public、protected和private
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					if (field == null || !field.getName().equals(fieldName)) continue;
					field.setAccessible(true);
					field.set(object, filedValue);
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static class Param {
		private Class<?> type;
		private Object value;
		
		public Param(Class<?> type, Object value) {
			this.type = type;
			this.value = value;
		}
		
		private static Class<?>[] getTypes(Param... params) {
			if (params == null || params.length == 0) return null;
			Class<?>[] types = new Class[params.length];
			for (int i = 0; i < params.length; i++) {
				types[i] = params[i].type;
			}
			return types;
		}
		
		private static Object[] getValues(Param... params) {
			if (params == null || params.length == 0) return null;
			Object[] values = new Object[params.length];
			for (int i = 0; i < params.length; i++) {
				values[i] = params[i].value;
			}
			return values;
		}
	}
}
