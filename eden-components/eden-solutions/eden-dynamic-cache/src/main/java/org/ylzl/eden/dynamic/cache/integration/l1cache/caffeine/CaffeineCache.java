package org.ylzl.eden.dynamic.cache.integration.l1cache.caffeine;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.level.AbstractAdaptingCache;
import org.ylzl.eden.dynamic.cache.level.L1Cache;
import org.ylzl.eden.dynamic.cache.loader.CacheLoader;
import org.ylzl.eden.dynamic.cache.sync.CacheSynchronizer;

import java.util.concurrent.Callable;

/**
 * Caffeine 缓存
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class CaffeineCache extends AbstractAdaptingCache implements L1Cache {

	private final Cache<Object, Object> cacheClient;

	public CaffeineCache(String cacheName, CacheConfig cacheConfig, Cache<Object, Object> cacheClient) {
		super(cacheName, cacheConfig);
		this.cacheClient = cacheClient;
	}

	/**
	 * 使用本地缓存提供类
	 *
	 * @return 本地缓存提供类
	 */
	@Override
	public Object getNativeCache() {
		return null;
	}

	/**
	 * 获取指定key的缓存项
	 *
	 * @param key 缓存Key
	 * @return 缓存值
	 */
	@Override
	public Object get(Object key) {
		return null;
	}

	/**
	 * 获取指定key的缓存项，如果缓存项不存在，则通过 {@code valueLoader} 获取值
	 *
	 * @param key         缓存Key
	 * @param valueLoader 缓存ValueLoader
	 * @return 缓存值
	 */
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		return null;
	}

	/**
	 * 设置指定key的缓存项
	 *
	 * @param key   缓存Key
	 * @param value 缓存值
	 */
	@Override
	public void put(Object key, Object value) {

	}

	/**
	 * 删除指定的缓存项
	 *
	 * @param key 存Key
	 */
	@Override
	public void evict(Object key) {

	}

	/**
	 * 清空缓存
	 */
	@Override
	public void clear() {

	}

	/**
	 * 获取缓存类型
	 *
	 * @return 缓存类型
	 */
	@Override
	public String getCacheType() {
		return null;
	}

	/**
	 * 缓存同步器
	 *
	 * @return
	 */
	@Override
	public CacheSynchronizer getCacheSynchronizer() {
		return null;
	}

	/**
	 * 缓存加载器
	 */
	@Override
	public <K, V> CacheLoader<K, V> getCacheLoader() {
		return null;
	}

	/**
	 * 清除指定key的本地缓存
	 *
	 * @param key
	 */
	@Override
	public void clearLocalCache(Object key) {

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
	 * 刷新指定key的缓存
	 *
	 * @param key
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
	 * 刷新指定key的过期缓存
	 *
	 * @param key
	 * @see L1Cache#isLoadingCache()
	 */
	@Override
	public void refreshExpireCache(Object key) {

	}

	/**
	 * 刷新所有过期缓存
	 *
	 * @see L1Cache#isLoadingCache()
	 */
	@Override
	public void refreshAllExpireCache() {

	}
}
