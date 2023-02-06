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

package org.ylzl.eden.common.cache;

import java.util.concurrent.Callable;

/**
 * 缓存接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface Cache {

	/**
	 * 获取缓存类型
	 *
	 * @return 缓存类型
	 */
	String getCacheType();

	/**
	 * 获取缓存名称
	 *
	 * @return 缓存名称
	 */
	String getName();

	/**
	 * 使用原生缓存实例
	 *
	 * @return 原生缓存实例
	 */
	Object getNativeCache();

	/**
	 * 获取指定key的缓存项
	 *
	 * @param key 缓存Key
	 * @return 缓存项
	 */
	Object get(Object key);

	/**
	 * 获取指定key的缓存项
	 *
	 * @param key 缓存Key
	 * @param type 缓存类型
	 * @return 缓存项
	 * @param <T> Value
	 */
	<T> T get(Object key, Class<T> type);

	/**
	 * 获取指定key的缓存项，如果缓存项不存在，则通过 {@code valueLoader} 获取值
	 *
	 * @param key 缓存Key
	 * @param valueLoader 缓存加载器
	 * @return 缓存项
	 * @param <T> Value
	 */
	<T> T get(Object key, Callable<T> valueLoader);

	/**
	 * 设置指定key的缓存项
	 *
	 * @param key 缓存Key
	 * @param value 缓存项
	 */
	void put(Object key, Object value);

	/**
	 * 设置指定key的缓存项，如果已存在则忽略
	 *
	 * @param key 缓存Key
	 * @param value 缓存项
	 * @return 缓存项
	 */
	default Object putIfAbsent(Object key, Object value) {
		Object existingValue = get(key);
		if (existingValue == null) {
			put(key, value);
		}
		return existingValue;
	}

	/**
	 * 删除指定的缓存项
	 *
	 * @param key 缓存Key
	 */
	void evict(Object key);

	/**
	 * 删除指定的缓存项，如果不存在则返回 false
	 *
	 * @param key 缓存Key
	 * @return 删除是否成功
	 */
	default boolean evictIfPresent(Object key) {
		Object existingValue = get(key);
		if (existingValue == null) {
			return false;
		}
		evict(key);
		return true;
	}

	/**
	 * 使缓存失效
	 *
	 * @return 是否执行成功
	 */
	default boolean invalidate() {
		clear();
		return false;
	}

	/**
	 * 清空缓存
	 */
	void clear();

	/**
	 * 判断Key是否存在
	 *
	 * @param key Key
	 * @return 是否存在
	 */
	boolean isExists(Object key);
}
