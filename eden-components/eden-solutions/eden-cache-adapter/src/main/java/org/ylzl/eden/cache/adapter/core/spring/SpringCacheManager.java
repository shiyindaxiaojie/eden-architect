package org.ylzl.eden.cache.adapter.core.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.ylzl.eden.cache.adapter.core.CacheConfig;
import org.ylzl.eden.cache.adapter.core.CacheSynchronizer;
import org.ylzl.eden.cache.adapter.core.listener.CacheExpiredListener;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存管理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class SpringCacheManager implements CacheManager {

	private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

	private final CacheConfig config;

	private final CacheSynchronizer synchronizer;

	private final CacheExpiredListener listener;

	@Override
	public Cache getCache(String name) {
		Cache cache = this.cacheMap.get(name);
		if (cache == null && this.config.isAllowDynamicCreate()) {
			synchronized (this.cacheMap) {
				cache = this.cacheMap.get(name);
				if (cache == null) {
					cache = null;
					this.cacheMap.put(name, cache);
				}
			}
		}
		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheMap.keySet());
	}

	private Cache createCacheWrapper() {
		org.ylzl.eden.cache.adapter.core.Cache cache =
	}
}
