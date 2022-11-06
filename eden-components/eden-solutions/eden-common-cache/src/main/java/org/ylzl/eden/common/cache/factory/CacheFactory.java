package org.ylzl.eden.common.cache.factory;

import org.jetbrains.annotations.NotNull;
import org.ylzl.eden.common.cache.builder.CacheBuilder;
import org.ylzl.eden.common.cache.core.Cache;
import org.ylzl.eden.common.cache.config.CacheSpec;

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
