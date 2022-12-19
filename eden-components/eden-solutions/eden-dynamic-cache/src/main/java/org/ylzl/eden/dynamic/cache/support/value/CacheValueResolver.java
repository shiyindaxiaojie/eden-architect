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

package org.ylzl.eden.dynamic.cache.support.value;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * 缓存值统一解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class CacheValueResolver {

	private static final String NULL_VALUE_NOT_ALLOWED = "Cache named '{}' is configured to not allow null values but null was provided";

	/**
	 * 转换为缓存值
	 *
	 * @param originalValue 原始值
	 * @param allowNullValue 是否允许为NULL
	 * @param cacheName 缓存名称
	 * @return 缓存值
	 */
	public static Object toCacheValue(Object originalValue, boolean allowNullValue, String cacheName) {
		if (originalValue == null) {
			if (allowNullValue) {
				return NullValue.INSTANCE;
			}
			throw new IllegalArgumentException(MessageFormatUtils.format(NULL_VALUE_NOT_ALLOWED, cacheName));
		}
		return originalValue;
	}

	/**
	 * 从缓存值解析为原始值
	 *
	 * @param cacheValue 缓存值
	 * @param allowNullValue 是否允许为NULL
	 * @return 原始值
	 */
	public static Object fromCacheValue(Object cacheValue, boolean allowNullValue) {
		if ((cacheValue instanceof NullValue) && allowNullValue) {
			return null;
		}
		return cacheValue;
	}
}
