package org.ylzl.eden.common.cache.expire;

import org.ylzl.eden.spring.framework.extension.SPI;

/**
 * 缓存失效监听器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI
public interface CacheExpiredListener<K, V> {

	/**
	 * 缓存过期触发
	 *
	 * @param key
	 * @param value
	 * @param cause
	 */
	void onExpired(K key, V value, CacheExpiredCause cause);
}
