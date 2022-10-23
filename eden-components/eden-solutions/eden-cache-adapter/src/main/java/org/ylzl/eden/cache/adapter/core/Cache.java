package org.ylzl.eden.cache.adapter.core;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * 缓存接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface Cache extends Serializable {

	/**
	 * 缓存名称
	 *
	 * @return
	 */
	String getName();

	/**
	 * Return the underlying native cache provider.
	 *
	 * @return
	 */
	Object getNativeCache();

	/**
	 * 获取指定key的缓存项
	 *
	 * @param key
	 * @return
	 */
	Object get(Object key);

	/**
	 * 获取指定key的缓存项，并返回指定类型的返回值
	 *
	 * @param key
	 * @param type
	 * @return
	 * @param <T>
	 */
	default <T> T get(Object key, Class<T> type) {
		Object value = get(key);
		if (value == null) {
			return null;
		}
		if (type != null && !type.isInstance(value)) {
			throw new IllegalStateException("Cached value `" + value + "`is not of required type `" + type.getName() + "`");
		}
		return (T) value;
	}

	/**
	 * 获取指定key的缓存项，如果缓存项不存在，则通过 {@code valueLoader} 获取值
	 *
	 * @param key
	 * @param valueLoader
	 * @return
	 * @param <T>
	 */
	<T> T get(Object key, Callable<T> valueLoader);

	/**
	 * 设置指定key的缓存项
	 *
	 * @param key
	 * @param value
	 */
	void put(Object key, Object value);

	/**
	 * 设置指定key的缓存项，如果已存在则忽略
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	default Object putIfAbsent(Object key, Object value) {
		Object existingValue = get(key);
		if (existingValue == null) {
			put(key, value);
		}
		return existingValue;
	}

	/**
	 * 删除指定的缓存项
	 *
	 * @param key
	 */
	void evict(Object key);


	/**
	 * 删除指定的缓存项，如果不存在则返回 false
	 *
	 * @param key
	 * @return
	 */
	default boolean evictIfPresent(Object key) {
		evict(key);
		return false;
	}

	/**
	 * 清空缓存
	 */
	void clear();

	/**
	 * 缓存无效化
	 *
	 * @return
	 */
	default boolean invalidate() {
		clear();
		return false;
	}

	/**
	 * 是否允许存储空值，避免缓存击穿
	 * @return
	 */
	boolean isAllowNullValues();


	long getNullValueExpireTimeSeconds();

	String getInstanceId();

	String getCacheType();

}
