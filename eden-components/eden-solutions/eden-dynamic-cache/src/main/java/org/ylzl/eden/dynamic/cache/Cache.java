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

package org.ylzl.eden.dynamic.cache;

import java.util.concurrent.Callable;

/**
 * 缓存接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface Cache {

	/**
	 * 获取缓存名称
	 *
	 * @return 缓存名称
	 */
	String getCacheName();

	/**
	 * 获取缓存类型
	 *
	 * @return 缓存类型
	 */
	String getCacheType();

	/**
	 * 使用原生缓存实例
	 *
	 * @return 原生缓存实例
	 */
	Object getNativeCache();

	/**
	 * 获取指定key的缓存项，如果缓存项不存在，则通过 {@code valueLoader} 获取值
	 *
	 * @param key 缓存Key
	 * @return 缓存值
	 */
	<K, V> V getIfPresent(K key);

	/**
	 * 获取指定key的缓存项，如果缓存项不存在，则通过 {@code valueLoader} 获取值
	 *
	 * @param key 缓存Key
	 * @param valueLoader 缓存加载器
	 * @return 缓存值
	 * @param <V> 泛型
	 */
	<K, V> V get(K key, Callable<V> valueLoader);

	/**
	 * 设置指定key的缓存项
	 *
	 * @param key 缓存Key
	 * @param value 缓存值
	 */
	<K, V> void put(K key, V value);

	/**
	 * 设置指定key的缓存项，如果已存在则忽略
	 *
	 * @param key 缓存Key
	 * @param value 缓存值
	 * @return 缓存值
	 */
	default <K, V> V putIfAbsent(K key, V value) {
		V existingValue = getIfPresent(key);
		if (existingValue == null) {
			put(key, value);
		}
		return existingValue;
	}

	/**
	 * 删除指定的缓存项，如果不存在则返回 false
	 *
	 * @param key 缓存Key
	 * @return 删除是否成功
	 */
	default <K> boolean invalidateIfPresent(K key) {
		Object existingValue = getIfPresent(key);
		if (existingValue == null) {
			return false;
		}
		invalidate(key);
		return true;
	}

	/**
	 * 删除指定的缓存项
	 *
	 * @param key 缓存Key
	 */
	<K> void invalidate(K key);

	/**
	 * 批量删除指定的缓存项
	 *
	 * @param keys 缓存Key集合
	 */
	<K> void invalidateAll(Iterable<K> keys);

	/**
	 * 清空缓存
	 */
	void invalidateAll();

	/**
	 * 判断是否允许存储 NULL，避免缓存击穿
	 *
	 * @return 是否允许存储 NULL
	 */
	boolean isAllowNullValue();

	/**
	 * 获取 NULL 的过期时间（秒）
	 *
	 * @return 过期时间
	 */
	long getNullValueExpireInSeconds();
}
