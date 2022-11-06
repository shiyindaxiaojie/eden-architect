package org.ylzl.eden.common.cache.builder;

import org.ylzl.eden.common.cache.core.Cache;
import org.ylzl.eden.common.cache.config.CacheConfig;
import org.ylzl.eden.common.cache.expire.CacheExpiredListener;
import org.ylzl.eden.common.cache.sync.CacheSynchronizer;

/**
 * 缓存构造器抽象
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class AbstractCacheBuilder<T extends Cache> implements CacheBuilder<T>  {

	private CacheConfig cacheConfig;

	private CacheExpiredListener<Object, Object> cacheExpiredListener;

	private CacheSynchronizer cacheSynchronizer;

	private volatile Object cacheClient;

	/**
	 * 获取缓存配置
	 *
	 * @return
	 */
	@Override
	public CacheConfig getCacheConfig() {
		return this.cacheConfig;
	}

	/**
	 * 设置缓存配置
	 *
	 * @param cacheConfig
	 * @return
	 */
	@Override
	public CacheBuilder<T> setCacheConfig(CacheConfig cacheConfig) {
		this.cacheConfig = cacheConfig;
		return this;
	}

	/**
	 * 获取缓存过期监听器
	 *
	 * @return
	 */
	@Override
	public CacheExpiredListener<Object, Object> getExpiredListener() {
		return this.cacheExpiredListener;
	}

	/**
	 * 设置缓存过期监听器
	 *
	 * @param cacheExpiredListener
	 * @return
	 */
	@Override
	public CacheBuilder<T> setExpiredListener(CacheExpiredListener<Object, Object> cacheExpiredListener) {
		this.cacheExpiredListener = cacheExpiredListener;
		return this;
	}

	/**
	 * 获取缓存同步器
	 *
	 * @return
	 */
	@Override
	public CacheSynchronizer getCacheSynchronizer() {
		return this.cacheSynchronizer;
	}

	/**
	 * 设置缓存同步器
	 *
	 * @param cacheSynchronizer
	 * @return
	 */
	@Override
	public CacheBuilder<T> setCacheSynchronizer(CacheSynchronizer cacheSynchronizer) {
		this.cacheSynchronizer = cacheSynchronizer;
		return this;
	}

	/**
	 * 获取实际执行的缓存客户端
	 *
	 * @return
	 */
	@Override
	public Object getCacheClient() {
		return this.cacheClient;
	}

	/**
	 * 设置实际执行的缓存客户端
	 *
	 * @param cacheClient
	 * @return
	 */
	@Override
	public CacheBuilder<T> setCacheClient(Object cacheClient) {
		this.cacheClient = cacheClient;
		return this;
	}
}
