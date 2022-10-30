package org.ylzl.eden.common.cache.integration.l1cache.caffeine;

import org.ylzl.eden.common.cache.core.config.CacheConfig;
import org.ylzl.eden.common.cache.core.level.AbstractAdaptingCache;
import org.ylzl.eden.common.cache.core.level.L1Cache;
import org.ylzl.eden.common.cache.core.loader.CacheLoader;
import org.ylzl.eden.common.cache.core.sync.CacheSynchronizer;

import java.util.concurrent.Callable;

/**
 * Caffeine 缓存
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CaffeineCache extends AbstractAdaptingCache implements L1Cache {

	public CaffeineCache(String cacheName, CacheConfig cacheConfig) {
		super(cacheName, cacheConfig);
	}

	@Override
	public Object getNativeCache() {
		return null;
	}

	@Override
	public Object get(Object key) {
		return null;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		return null;
	}

	@Override
	public void put(Object key, Object value) {

	}

	@Override
	public void evict(Object key) {

	}

	@Override
	public void clear() {

	}

	@Override
	public String getCacheType() {
		return null;
	}

	@Override
	public CacheSynchronizer getCacheSynchronizer() {
		return null;
	}

	@Override
	public <K, V> CacheLoader<K, V> getCacheLoader() {
		return null;
	}

	@Override
	public void clearLocalCache(Object key) {

	}

	@Override
	public boolean isLoadingCache() {
		return false;
	}

	@Override
	public void refresh(Object key) {

	}

	@Override
	public void refreshAll() {

	}

	@Override
	public void refreshExpireCache(Object key) {

	}

	@Override
	public void refreshAllExpireCache() {

	}
}
