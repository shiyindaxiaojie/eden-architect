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

package org.ylzl.eden.dynamic.cache.factory;

import org.jetbrains.annotations.NotNull;
import org.ylzl.eden.dynamic.cache.builder.CacheBuilder;
import org.ylzl.eden.dynamic.cache.Cache;
import org.ylzl.eden.dynamic.cache.config.CacheSpec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class CacheFactory {

	private static final Map<String, ConcurrentHashMap<String, Cache>> CACHE_MAP = new ConcurrentHashMap<>(32);

	private static final Map<String, ConcurrentHashMap<String, CacheSpec>> CACHE_SPEC_MAP = new ConcurrentHashMap<>(32);

	private static final byte[] checkCacheMapLock = new byte[0];

	private static final byte[] buildCacheLock = new byte[0];

	/**
	 * 获取缓存实例
	 *
	 * @param cacheType
	 * @param cacheName
	 * @return
	 */
	public static Cache getCache(String cacheType, String cacheName) {
		ConcurrentHashMap<String, Cache> cacheMap = CACHE_MAP.get(cacheType);
		if (cacheMap == null) {
			return null;
		}
		return cacheMap.get(cacheName);
	}

	/**
	 * 获取缓存配置
	 *
	 * @param cacheType
	 * @param cacheName
	 * @return
	 */
	public static CacheSpec getCacheSpec(String cacheType, String cacheName) {
		ConcurrentHashMap<String, CacheSpec> cacheSpecMap = CACHE_SPEC_MAP.get(cacheType);
		if (cacheSpecMap == null) {
			return null;
		}
		return cacheSpecMap.get(cacheName);
	}

	/**
	 * 获取或创建缓存实例
	 *
	 * @param cacheType
	 * @param cacheName
	 * @param cacheBuilder
	 * @return
	 */
	public static Cache getOrCreateCache(@NotNull String cacheType, @NotNull String cacheName,
										 CacheBuilder<?> cacheBuilder) {
		ConcurrentHashMap<String, Cache> cacheMap = CACHE_MAP.get(cacheType);
		if (cacheMap == null) {
			synchronized (checkCacheMapLock) {
				cacheMap = CACHE_MAP.get(cacheType);
				if (cacheMap == null) {
					cacheMap = new ConcurrentHashMap<>();
					CACHE_MAP.put(cacheType, cacheMap);
				}
			}
		}

		Cache cache = cacheMap.get(cacheName);
		if (cache != null) {
			return cache;
		}
		synchronized (buildCacheLock) {
			cache = cacheMap.get(cacheName);
			if (cache != null) {
				return cache;
			}

			buildCacheSpec(cacheType, cacheName, cacheBuilder);
			cache = cacheBuilder.build(cacheName);
			cacheMap.put(cacheName, cache);
			return cache;
		}
	}

	/**
	 * 构建缓存配置
	 *
	 * @param cacheType
	 * @param cacheName
	 * @param cacheBuilder
	 */
	private static void buildCacheSpec(String cacheType, String cacheName, CacheBuilder<?> cacheBuilder) {
		ConcurrentHashMap<String, CacheSpec> cacheSpecMap = CACHE_SPEC_MAP.get(cacheType);
		if (cacheSpecMap == null) {
			cacheSpecMap = CACHE_SPEC_MAP.get(cacheType);
			if (cacheSpecMap == null) {
				cacheSpecMap = new ConcurrentHashMap<>();
				CACHE_SPEC_MAP.put(cacheType, cacheSpecMap);
			}
		}
		CacheSpec cacheSpec = cacheBuilder.parseSpec(cacheName);
		if (cacheSpec != null) {
			cacheSpecMap.put(cacheName, cacheSpec);
		}
	}
}
