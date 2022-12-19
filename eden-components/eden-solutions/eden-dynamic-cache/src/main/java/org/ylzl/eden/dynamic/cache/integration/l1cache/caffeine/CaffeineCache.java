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

package org.ylzl.eden.dynamic.cache.integration.l1cache.caffeine;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.dynamic.cache.CacheType;
import org.ylzl.eden.dynamic.cache.L1Cache;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.support.AbstractAdaptingCache;

import java.util.concurrent.Callable;

/**
 * Caffeine 缓存
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
@Slf4j
public class CaffeineCache extends AbstractAdaptingCache implements L1Cache {

	private final Cache<?, ?> caffeineCache;

	public CaffeineCache(String cacheName, CacheConfig cacheConfig, Cache<?, ?> caffeineCache) {
		super(cacheName, cacheConfig);
		this.caffeineCache = caffeineCache;
	}

	/**
	 * 获取缓存类型
	 *
	 * @return 缓存类型
	 */
	@Override
	public String getCacheType() {
		return CacheType.CAFFEINE.name();
	}

	/**
	 * 使用本地缓存提供类
	 * <p>针对定制化需求暴露底层的客户端实例操作</p>
	 *
	 * @return 本地缓存提供类
	 */
	@Override
	public Cache<?, ?> getNativeCache() {
		return this.caffeineCache;
	}

	/**
	 * 获取指定key的缓存项，如果缓存项不存在，则通过 {@code valueLoader} 获取值
	 *
	 * @param key 缓存Key
	 * @return 缓存值
	 */
	@Override
	public <K, V> V getIfPresent(K key) {
		return null;
	}

	/**
	 * 获取指定key的缓存项，如果缓存项不存在，则通过 {@code valueLoader} 获取值
	 *
	 * @param key         缓存Key
	 * @param valueLoader 缓存加载器
	 * @return 缓存值
	 */
	@Override
	public <K, V> V get(K key, Callable<V> valueLoader) {
		return null;
	}

	/**
	 * 设置指定key的缓存项
	 *
	 * @param key   缓存Key
	 * @param value 缓存值
	 */
	@Override
	public <K, V> void put(K key, V value) {

	}

	/**
	 * 删除指定的缓存项
	 *
	 * @param key 缓存Key
	 */
	@Override
	public <K> void invalidate(K key) {

	}

	/**
	 * 批量删除指定的缓存项
	 *
	 * @param keys 缓存Key集合
	 */
	@Override
	public <K> void invalidateAll(Iterable<K> keys) {

	}

	/**
	 * 清空缓存
	 */
	@Override
	public void invalidateAll() {

	}

	/**
	 * 是否为 LoadingCache
	 *
	 * @see LoadingCache
	 * @see AsyncLoadingCache
	 * @see com.google.common.cache.LoadingCache
	 */
	@Override
	public boolean isLoadingCache() {
		return false;
	}

	/**
	 * 刷新指定 key 的缓存
	 *
	 * @param key 指定 key
	 * @see L1Cache#isLoadingCache()
	 */
	@Override
	public void refresh(Object key) {

	}

	/**
	 * 刷新所有缓存
	 *
	 * @see L1Cache#isLoadingCache()
	 */
	@Override
	public void refreshAll() {

	}

	/**
	 * 刷新指定 key 的过期缓存
	 *
	 * @param key 指定 key
	 * @see L1Cache#isLoadingCache()
	 */
	@Override
	public void refreshExpiredCache(Object key) {

	}

	/**
	 * 刷新所有过期缓存
	 *
	 * @see L1Cache#isLoadingCache()
	 */
	@Override
	public void refreshAllExpiredCache() {

	}

	/**
	 * 清除指定 key 的本地缓存
	 *
	 * @param key 指定 key
	 */
	@Override
	public void invalidateLocalCache(Object key) {

	}
}
