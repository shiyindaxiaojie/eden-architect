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

package org.ylzl.eden.spring.framework.error.util;

import lombok.NonNull;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.util.Assert;
import org.ylzl.eden.spring.framework.error.ErrorCodeLoader;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具封装
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class AssertUtils extends Assert {

	public static void doesNotContain(@NonNull String textToSearch, String substring, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
									  Object... placeholders) {
		doesNotContain(textToSearch, substring, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void hasLength(@NonNull String expression, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
								 Object... placeholders) {
		hasLength(expression, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void hasText(String text, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
							   Object... placeholders) {
		hasText(text, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void isInstanceOf(Class<?> type, @NonNull Object obj, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
									Object... placeholders) {
		isInstanceOf(type, obj, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void isNull(Object object, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
							  Object... placeholders) {
		isNull(object, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void notNull(Object object, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
							   Object... placeholders) {
		notNull(object, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void isTrue(boolean expression, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
							  Object... placeholders) {
		isTrue(expression, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void noNullElements(@NonNull Collection<?> collection, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
									  Object... placeholders) {
		noNullElements(collection, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void notEmpty(@NonNull Object[] array, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
								Object... placeholders) {
		notEmpty(array, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void notEmpty(@NonNull Collection<?> collection, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
								Object... placeholders) {
		notEmpty(collection, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void notEmpty(@NonNull Map<?, ?> map, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
								Object... placeholders) {
		notEmpty(map, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void isAssignable(Class<?> superType, @NonNull Class<?> subType, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
									Object... placeholders) {
		isAssignable(superType, subType, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}

	public static void state(boolean expression, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
							 Object... placeholders) {
		state(expression, ErrorCodeLoader.getErrMessage(errCode, placeholders));
	}
}
