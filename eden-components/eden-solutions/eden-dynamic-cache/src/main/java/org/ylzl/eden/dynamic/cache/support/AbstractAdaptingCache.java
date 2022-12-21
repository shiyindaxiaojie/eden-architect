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

package org.ylzl.eden.dynamic.cache.support;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.support.NullValue;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.dynamic.cache.Cache;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;

/**
 * 缓存接口适配器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public abstract class AbstractAdaptingCache implements Cache {

	private static final String NOT_ALLOWED_NULL_VALUES =
		"Cache '{}' is configured to not allow null values but null was provided";

	private final String cacheName;

	private final CacheConfig cacheConfig;

	/**
	 * 获取缓存名称
	 *
	 * @return 缓存名称
	 */
	@Override
	public String getName() {
		return this.cacheName;
	}

	/**
	 * 获取缓存配置
	 *
	 * @return 缓存配置
	 */
	protected CacheConfig getCacheConfig() {
		return this.cacheConfig;
	}

	/**
	 * 判断是否允许存储 NULL，避免缓存击穿
	 *
	 * @return 是否允许存储 NULL
	 */
	protected boolean isAllowNullValues() {
		return cacheConfig.isAllowNullValues();
	}

	/**
	 * 从缓存值解析为原始值
	 *
	 * @param storeValue 缓存值
	 * @return 原始值
	 */
	protected <V> V fromStoreValue(V storeValue) {
		if (this.isAllowNullValues() && storeValue == NullValue.INSTANCE) {
			return null;
		}
		return storeValue;
	}

	/**
	 * 转换为缓存值
	 *
	 * @param userValue 原始值
	 * @return 缓存值
	 */
	protected <V> Object toStoreValue(V userValue) {
		if (userValue == null) {
			if (this.isAllowNullValues()) {
				return NullValue.INSTANCE;
			}
			throw new IllegalArgumentException(MessageFormatUtils.format(NOT_ALLOWED_NULL_VALUES, this.getName()));
		}
		return userValue;
	}

	/**
	 * 构建缓存Key
	 *
	 * @param key Key
	 * @return Key
	 */
	protected String buildKey(Object key) {
		return this.getName() + this.getCacheConfig().getKeySeparator() + key;
	}
}
