package org.ylzl.eden.cache.adapter.core.factory;

import org.ylzl.eden.cache.adapter.core.Cache;
import org.ylzl.eden.cache.adapter.core.CacheSpec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class CacheFactory {

	private static final Map<String, ConcurrentHashMap<String, Cache>> CACHE_MAP = new ConcurrentHashMap<>(32);

	private static final Map<String, ConcurrentHashMap<String, CacheSpec>> CACHE_SPEC_MAP = new ConcurrentHashMap<>(32);

	public static Cache getCache(String cacheType, String cacheName) {
		ConcurrentHashMap<String, Cache> cacheMap = CACHE_MAP.get(cacheType);
		if (null == cacheMap) {
			return null;
		}
		return cacheMap.get(cacheName);
	}

	public static CacheSpec getCacheSpec(String cacheType, String cacheName) {
		ConcurrentHashMap<String, CacheSpec> cacheSpecMap = CACHE_SPEC_MAP.get(cacheType);
		if (null == cacheSpecMap) {
			return null;
		}
		return cacheSpecMap.get(cacheName);
	}
}
