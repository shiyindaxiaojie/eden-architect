/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.commons.lang;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.ylzl.eden.commons.json.JacksonUtils;
import org.ylzl.eden.commons.lang.math.NumberUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 对象工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 * @see org.apache.commons.lang3.ObjectUtils
 */
@UtilityClass
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

	public static boolean isNull(Object object) {
		if (isEmpty(object)) {
			return true;
		}
		return Strings.NULL.equals(String.valueOf(object));
	}

	public static boolean isNotNull(Object object) {
		return !isNull(object);
	}

	public static String trimToString(Object object) {
		if (object == null) {
			return Strings.EMPTY;
		}
		return StringUtils.trimToEmpty(String.valueOf(object));
	}

	public static String trimToString(Object object, String defaultValue) {
		if (object == null) {
			return defaultValue;
		}
		return trimToString(object);
	}

	public static int trimToInt(Object object) {
		if (object == null) {
			return 0;
		}
		return NumberUtils.toInt(trimToString(object));
	}

	public static int trimToInt(Object object, int defaultValue) {
		if (object == null) {
			return defaultValue;
		}
		return NumberUtils.toInt(trimToString(object));
	}

	public static Object toObject(CharSequence cs) throws IOException, ClassNotFoundException {
		if (isEmpty(cs)) {
			return null;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(cs.toString()));
		ObjectInputStream ois = new ObjectInputStream(bis);
		return ois.readObject();
	}

	public static Object cast(@NonNull Object object, @NonNull Class<?> clazz) {
		String value = trimToString(object);
		if (clazz.isPrimitive() || ClassUtils.isWrapClass(clazz)) { // 基本类型或者包装类型
			String simpleName = clazz.getSimpleName();
			for (PrimitiveType primitiveType : PrimitiveType.values()) {
				if (primitiveType.name().equalsIgnoreCase(simpleName)
					|| clazz.equals(primitiveType.getWrapperClass())) {
					return primitiveType.cast(value);
				}
			}
		}
		if (CharSequence.class.isAssignableFrom(clazz)) { // 字符串
			return value;
		}
		if (clazz.isArray()) { // 数组
			String[] values = value.split(Strings.COMMA);
			int length = values.length;
			Object array = Array.newInstance(clazz.getComponentType(), length);
			for (int i = 0; i < length; i++) {
				Array.set(array, i, values[i]);
			}
			return array;
		}
		return clazz.cast(object);
	}

	public static Object cast(@NonNull Object object, @NonNull Field field) throws IOException {
		String value = trimToString(object);
		Class<?> clazz = field.getType();
		if (Iterable.class.isAssignableFrom(clazz)) { // 集合
			Type genericType = field.getGenericType();
			if (genericType instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) genericType;
				Type[] listActualTypeArguments = parameterizedType.getActualTypeArguments();
				Class<?> listClass = (Class<?>) listActualTypeArguments[0];
				return JacksonUtils.toList(value, listClass);
			}
		}
		if (Map.class.isAssignableFrom(clazz)) { // 映射
			return JacksonUtils.toMap(value);
		}
		return cast(object, clazz);
	}
}
