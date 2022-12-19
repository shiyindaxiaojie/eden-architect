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

import java.util.Collection;
import java.util.Set;

/**
 * 一级缓存接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface L1Cache extends Cache {

	/**
	 * 是否为 LoadingCache
	 *
	 * @see com.github.benmanes.caffeine.cache.LoadingCache
	 * @see com.github.benmanes.caffeine.cache.AsyncLoadingCache
	 * @see com.google.common.cache.LoadingCache
	 */
	boolean isLoadingCache();

	/**
	 * 刷新指定 key 的缓存
	 *
	 * @param key 指定 key
	 * @see L1Cache#isLoadingCache()
	 */
	void refresh(Object key);

	/**
	 * 刷新所有缓存
	 * @see L1Cache#isLoadingCache()
	 */
	void refreshAll();

	/**
	 * 刷新指定 key 的过期缓存
	 *
	 * @param key 指定 key
	 * @see L1Cache#isLoadingCache()
	 */
	void refreshExpiredCache(Object key);

	/**
	 * 刷新所有过期缓存
	 *
	 * @see L1Cache#isLoadingCache()
	 */
	void refreshAllExpiredCache();

	/**
	 * 清除指定 key 的本地缓存
	 *
	 * @param key 指定 key
	 */
	void invalidateLocalCache(Object key);

	/**
	 * 获取所有缓存 Key
	 *
	 * @return 缓存 Key 集合
	 */
	default Set<Object> keys() {
		return null;
	}

	/**
	 * 获取所有缓存项
	 *
	 * @return 缓存 Key 集合
	 */
	default Collection<Object> values() {
		return null;
	}
}
