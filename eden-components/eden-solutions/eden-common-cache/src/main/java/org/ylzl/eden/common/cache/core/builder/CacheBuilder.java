package org.ylzl.eden.common.cache.core.builder;

import org.ylzl.eden.common.cache.core.Cache;
import org.ylzl.eden.common.cache.core.config.CacheConfig;
import org.ylzl.eden.common.cache.core.config.CacheSpec;
import org.ylzl.eden.common.cache.core.expire.CacheExpiredListener;
import org.ylzl.eden.common.cache.core.sync.CacheSynchronizer;
import org.ylzl.eden.spring.framework.extension.SPI;

import java.io.Serializable;

/**
 * 缓存构造器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI
public interface CacheBuilder<T extends Cache> extends Serializable {

	/**
	 * 构建指定名称的缓存对象
	 *
	 * @param cacheName
	 * @return
	 */
	T build(String cacheName);

	/**
	 * 解析指定名称的缓存配置
	 *
	 * @param cacheName
	 * @return
	 */
	CacheSpec parseSpec(String cacheName);

	/**
	 * 获取缓存配置
	 *
	 * @return
	 */
	CacheConfig getCacheConfig();

	/**
	 * 设置缓存配置
	 *
	 * @param cacheConfig
	 * @return
	 */
	CacheBuilder<T> setCacheConfig(CacheConfig cacheConfig);

	/**
	 * 获取缓存过期监听器
	 *
	 * @return
	 */
	CacheExpiredListener<Object, Object> getExpiredListener();

	/**
	 * 设置缓存过期监听器
	 *
	 * @param cacheExpiredListener
	 * @return
	 */
	CacheBuilder<T> setExpiredListener(CacheExpiredListener<Object, Object> cacheExpiredListener);

	/**
	 * 获取缓存同步器
	 *
	 * @return
	 */
	CacheSynchronizer getCacheSynchronizer();

	/**
	 * 设置缓存同步器
	 *
	 * @param cacheSynchronizer
	 * @return
	 */
	CacheBuilder<T> setCacheSynchronizer(CacheSynchronizer cacheSynchronizer);

	/**
	 * 获取实际执行的缓存客户端
	 *
	 * @return
	 */
	Object getCacheClient();

	/**
	 * 设置实际执行的缓存客户端
	 *
	 * @param cacheClient
	 * @return
	 */
	CacheBuilder<T> setCacheClient(Object cacheClient);
}
