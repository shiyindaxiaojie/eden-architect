package org.ylzl.eden.cache.adapter.core.listener;

/**
 * 缓存失效监听器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface CacheExpiredListener<K, V> {

	/**
	 * 缓存过期触发
	 *
	 * @param key
	 * @param value
	 * @param cause
	 */
	void onExpired(K key, V value, String cause);
}
