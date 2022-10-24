package org.ylzl.eden.cache.adapter.core.level;

import org.ylzl.eden.cache.adapter.core.Cache;
import org.ylzl.eden.cache.adapter.core.loader.CacheLoader;
import org.ylzl.eden.cache.adapter.core.sync.CacheSynchronizer;

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
	 * 缓存同步器
	 *
	 * @return
	 */
	CacheSynchronizer getCacheSynchronizer();

	/**
	 * 缓存加载器
	 */
	<K, V> CacheLoader<K, V> getCacheLoader();

	/**
	 * 清除指定key的本地缓存
	 *
	 * @param key
	 */
	void clearLocalCache(Object key);

	/**
	 * 是否为 LoadingCache
	 *
	 * @see com.github.benmanes.caffeine.cache.LoadingCache
	 * @see com.github.benmanes.caffeine.cache.AsyncLoadingCache
	 * @see com.google.common.cache.LoadingCache
	 */
	boolean isLoadingCache();

	/**
	 * 刷新指定key的缓存
	 *
	 * @param key
	 * @see L1Cache#isLoadingCache()
	 */
	void refresh(Object key);

	/**
	 * 刷新所有缓存
	 * @see L1Cache#isLoadingCache()
	 */
	void refreshAll();

	/**
	 * 刷新指定key的过期缓存
	 *
	 * @param key
	 * @see L1Cache#isLoadingCache()
	 */
	void refreshExpireCache(Object key);

	/**
	 * 刷新所有过期缓存
	 *
	 * @see L1Cache#isLoadingCache()
	 */
	void refreshAllExpireCache();

	/**
	 * 获取所有缓存Key
	 *
	 * @return
	 */
	default Set<Object> keys() {
		return null;
	}

	/**
	 * 获取所有缓存项
	 */
	default Collection<Object> values() {
		return null;
	}
}
