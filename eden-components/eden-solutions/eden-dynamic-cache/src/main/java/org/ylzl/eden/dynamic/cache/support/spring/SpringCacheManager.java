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

package org.ylzl.eden.dynamic.cache.support.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.expire.CacheRemovalListener;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Spring 缓存管理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class SpringCacheManager implements CacheManager {

	private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

	private final CacheConfig config;

	private final CacheRemovalListener removalListener;

	private final Object cacheClient;

	/**
	 * 获取缓存实例
	 *
	 * @param name 缓存名称
	 * @return 缓存实例
	 */
	@Override
	public Cache getCache(@NotNull String name) {
		Cache cache = this.cacheMap.get(name);
		if (cache == null && this.config.isDynamic()) {
			synchronized (this.cacheMap) {
				cache = this.cacheMap.computeIfAbsent(name, n -> this.createSpringCache(config.getCacheType(), n));
			}
		}
		return cache;
	}

	/**
	 * 获取缓存名称集合
	 *
	 * @return 缓存名称集合
	 */
	@Override
	public @NotNull Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheMap.keySet());
	}

	/**
	 * 创建 Spring 包装缓存
	 *
	 * @see org.ylzl.eden.dynamic.cache.Cache
	 * @param cacheType 缓存类型
	 * @param cacheName 缓存名称
	 * @return 缓存实例
	 */
	private Cache createSpringCache(String cacheType, String cacheName) {
		org.ylzl.eden.dynamic.cache.Cache cache = createCache(cacheType, cacheName);
		return new SpringCache(this.config.isAllowNullValue(), cacheName, cache);
	}

	/**
	 * 创建 {@code Cache} 缓存实例
	 *
	 * @see org.ylzl.eden.dynamic.cache.Cache
	 * @param cacheType 缓存类型
	 * @param cacheName 缓存名称
	 * @return 缓存实例
	 */
	private org.ylzl.eden.dynamic.cache.Cache createCache(String cacheType, String cacheName) {
		/*org.ylzl.eden.dynamic.cache.Cache cache = CacheFactory.getCache(cacheType, cacheName);
		if (cache != null) {
			return cache;
		}*/

		/*CacheBuilder cacheBuilder = ExtensionLoader.getExtensionLoader(CacheBuilder.class).getExtension(cacheType);
		cacheBuilder.cacheName(cacheName)
				.evictionListener(removalListener)
				.initialCapacity();
		return CacheFactory.getOrCreateCache(cacheType, cacheName, cacheBuilder);*/
		return null;
	}
}
