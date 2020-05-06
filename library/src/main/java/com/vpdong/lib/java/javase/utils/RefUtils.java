package com.vpdong.lib.java.javase.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class RefUtils {
	private RefUtils() throws Exception {
		throw new IllegalAccessException("can not to init instance for ref utils");
	}
	
	public static Object newInstance(String classStr, Class<?>[] paramTypes, Object... paramVals) {
		try {
			Class<?> clazz = Class.forName(classStr);
			Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
			constructor.setAccessible(true);
			return constructor.newInstance(paramVals);
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
	
	public static Object invokeMethod(Object object, String methodName,
	                                  Class<?>[] argsTypes, Object... argsValues) {
		if (object == null || methodName == null) return null;
		try {
			for (Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
				Method method = null;
				try {// getDeclaredMethod获取的是类自身声明的所有方法，包含public、protected和private
					method = clazz.getDeclaredMethod(methodName, argsTypes);
				} catch (NoSuchMethodException ignored) {
					// ignored
				}
				if (method == null) continue;
				return method.invoke(object, argsValues);
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
}
