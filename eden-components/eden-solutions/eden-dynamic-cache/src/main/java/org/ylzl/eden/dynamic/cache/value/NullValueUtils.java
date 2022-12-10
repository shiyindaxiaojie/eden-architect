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

package org.ylzl.eden.dynamic.cache.value;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * NullValue 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class NullValueUtils {

	/**
	 * 转换为存储值
	 *
	 * @param value
	 * @param allowNullValues
	 * @param cacheName
	 * @return
	 */
	public static Object toStoreValue(Object value, boolean allowNullValues, String cacheName) {
		if (value == null) {
			if (allowNullValues) {
				return NullValue.INSTANCE;
			}
			throw new IllegalArgumentException(MessageFormatUtils.format(
				"Cache ‘{}‘ is configured to not allow null values but null was provided", cacheName));
		}
		return value;
	}

	/**
	 * 从存储值解析为具体值
	 */
	public static Object fromStoreValue(Object storeValue, boolean allowNullValues) {
		if (storeValue instanceof NullValue && allowNullValues) {
			return null;
		}
		return storeValue;
	}
}
