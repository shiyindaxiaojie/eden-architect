package org.ylzl.eden.common.cache.core.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.ylzl.eden.common.cache.core.builder.CacheBuilder;
import org.ylzl.eden.common.cache.core.config.CacheConfig;
import org.ylzl.eden.common.cache.core.factory.CacheFactory;
import org.ylzl.eden.common.cache.core.listener.CacheExpiredListener;
import org.ylzl.eden.common.cache.core.sync.CacheSynchronizer;
import org.ylzl.eden.spring.framework.extension.ExtensionLoader;

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

	private final CacheSynchronizer synchronizer;

	private final CacheExpiredListener<?, ?> listener;

	private final Object cacheClient;

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

	@Override
	public @NotNull Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheMap.keySet());
	}

	private Cache createSpringCache(String cacheType, String cacheName) {
		org.ylzl.eden.common.cache.core.Cache cache = CacheFactory.getCache(cacheType, cacheName);
		return new SpringCache(this.config.isAllowNullValues(), cacheName, cache);
	}

	private org.ylzl.eden.common.cache.core.Cache getOrCreateCache(String cacheType, String cacheName) {
		org.ylzl.eden.common.cache.core.Cache cache = CacheFactory.getCache(cacheType, cacheName);
		if (null != cache) {
			return cache;
		}

		CacheBuilder<?> cacheBuilder = ExtensionLoader.getExtensionLoader(CacheBuilder.class).getExtension(cacheType);
		cacheBuilder.setCacheConfig(this.config)
			.setCacheSynchronizer(this.synchronizer)
			.setExpiredListener(listener)
			.setCacheClient(this.cacheClient);
		return CacheFactory.getOrCreateCache(cacheType, cacheName, cacheBuilder);
	}
}
