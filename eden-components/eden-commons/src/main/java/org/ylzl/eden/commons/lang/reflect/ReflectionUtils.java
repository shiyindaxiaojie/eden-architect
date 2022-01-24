/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.commons.lang.reflect;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具集
 *
 * @author gyl
 * @since 2.4.x
 */
@SuppressWarnings("unchecked")
@UtilityClass
public class ReflectionUtils {

	public static <T> T newProxyInstance(Class<T> clazz, InvocationHandler handler) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler);
	}

	public static void setField(@NonNull Object destObj, @NonNull Field field, Object... argument)
		throws IllegalArgumentException, IllegalAccessException {
		setAccessible(field);
		field.set(destObj, argument);
	}

	public static void setField(
		@NonNull Object destObj, @NonNull String fieldName, Object... argument)
		throws IllegalArgumentException, IllegalAccessException, SecurityException {
		Field field = getDeclaredField(destObj, fieldName);
		setField(destObj, field, argument);
	}

	public static Object getField(@NonNull Object destObj, @NonNull Field field)
		throws IllegalArgumentException, IllegalAccessException {
		setAccessible(field);
		return field.get(destObj);
	}

	public static Object getField(@NonNull Object destObj, @NonNull String fieldName)
		throws IllegalArgumentException, IllegalAccessException, SecurityException {
		Field field = getDeclaredField(destObj, fieldName);
		return getField(destObj, field);
	}

	public static Field getDeclaredFields(@NonNull Class<?> clazz, @NonNull String fieldName)
		throws SecurityException {
		List<Field> fields = getDeclaredFields(clazz);
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	public static Field getDeclaredField(@NonNull Object destObj, @NonNull String fieldName)
		throws SecurityException {
		return getDeclaredFields(destObj.getClass(), fieldName);
	}

	public static List<Field> getDeclaredFields(@NonNull Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		Class<?> tempClass = clazz;
		while (tempClass != null) {
			fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			tempClass = tempClass.getSuperclass();
		}
		return fields;
	}

	public static Object invokeMethod(
		@NonNull Object object,
		@NonNull String methodName,
		Class<?>[] parameterTypes,
		Object[] parameters)
		throws SecurityException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException {
		Method method = getDeclaredMethod(object.getClass(), methodName, parameterTypes);
		setAccessible(method);
		return method.invoke(object, parameters);
	}

	public static Method getDeclaredMethod(
		@NonNull Class<?> clazz, @NonNull String methodName, Class<?>... parameterTypes)
		throws SecurityException {
		List<Method> methodList = getDeclaredMethods(clazz);
		for (Method method : methodList) {
			if (method.getName().equals(methodName)) {
				if (parameterTypes.length == 0 || method.getParameterTypes() == null) {
					return method;
				}
				if (parameterTypes.getClass().isAssignableFrom(method.getParameterTypes().getClass())) {
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 获取实际的泛型类型
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> Class<T> getActualTypeArgument(@NonNull Class<?> clazz) {
		Type type = clazz.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			return (Class<T>) parameterizedType.getActualTypeArguments()[0];
		}
		throw new RuntimeException();
	}

	public static Method getSetter(
		@NonNull Class<?> clazz, @NonNull Field field, Class<?>... parameterTypes)
		throws SecurityException {
		String fieldName = field.getName();
		String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return getDeclaredMethod(clazz, methodName, parameterTypes);
	}

	public static Method getGetter(@NonNull Class<?> clazz, @NonNull final Field field)
		throws SecurityException {
		String fieldName = field.getName();
		String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return getDeclaredMethod(clazz, methodName);
	}

	public static List<Method> getDeclaredMethods(@NonNull Class<?> clazz) {
		List<Method> methodList = new ArrayList<>();
		Class<?> tempClass = clazz;
		while (tempClass != null) {
			methodList.addAll(Arrays.asList(tempClass.getDeclaredMethods()));
			tempClass = tempClass.getSuperclass();
		}
		return methodList;
	}

	public static <T> Class<T> getSuperClassGenricType(@NonNull Class<?> clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return (Class<T>) Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0 || !(params[index] instanceof Class)) {
			return (Class<T>) Object.class;
		}
		return (Class<T>) params[index];
	}

	public static void setAccessible(@NonNull Field field) {
		Class<?> cls = field.getDeclaringClass();
		if ((!isPublic(field) || !isPublic(cls) || isFinal(field)) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	public static void setAccessible(@NonNull Method method) {
		Class<?> cls = method.getDeclaringClass();
		if ((!isPublic(method) || !isPublic(cls)) && !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	public static boolean isPublic(@NonNull Class<?> cls) {
		return Modifier.isPublic(cls.getModifiers());
	}

	public static boolean isPublic(@NonNull Method method) {
		return Modifier.isPublic(method.getModifiers());
	}

	public static boolean isPublic(@NonNull Field field) {
		return Modifier.isPublic(field.getModifiers());
	}

	public static boolean isProtected(@NonNull Class<?> cls) {
		return Modifier.isProtected(cls.getModifiers());
	}

	public static boolean isProtected(@NonNull Method method) {
		return Modifier.isProtected(method.getModifiers());
	}

	public static boolean isProtected(@NonNull Field field) {
		return Modifier.isProtected(field.getModifiers());
	}

	public static boolean isPrivate(@NonNull Class<?> cls) {
		return Modifier.isPrivate(cls.getModifiers());
	}

	public static boolean isPrivate(@NonNull Method method) {
		return Modifier.isPrivate(method.getModifiers());
	}

	public static boolean isPrivate(@NonNull Field field) {
		return Modifier.isPrivate(field.getModifiers());
	}

	public static boolean isFinal(@NonNull Class<?> cls) {
		return Modifier.isFinal(cls.getModifiers());
	}

	public static boolean isFinal(@NonNull Method method) {
		return Modifier.isFinal(method.getModifiers());
	}

	public static boolean isFinal(@NonNull Field field) {
		return Modifier.isFinal(field.getModifiers());
	}
}
