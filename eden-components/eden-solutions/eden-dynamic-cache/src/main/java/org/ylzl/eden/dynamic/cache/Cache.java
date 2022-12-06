package org.ylzl.eden.dynamic.cache;

import org.ylzl.eden.dynamic.cache.value.NullValueUtils;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * 缓存接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface Cache extends Serializable {

	/**
	 * 获取缓存名称
	 *
	 * @return 缓存名称
	 */
	String getCacheName();

	/**
	 * 使用本地缓存提供类
	 *
	 * @return 本地缓存提供类
	 */
	Object getNativeCache();

	/**
	 * 获取指定key的缓存项
	 *
	 * @param key 缓存Key
	 * @return 缓存值
	 */
	Object get(Object key);

	/**
	 * 获取指定key的缓存项，并返回指定类型的返回值
	 *
	 * @param key 缓存Key
	 * @param type 缓存Class
	 * @return 缓存值
	 * @param <T> 泛型
	 */
	default <T> T get(Object key, Class<T> type) {
		Object value = get(key);
		if (value == null) {
			return null;
		}
		if (type != null && !type.isInstance(value)) {
			throw new IllegalStateException(MessageFormatUtils.format(
				"Cached value ‘{}‘ is not of required type ‘{}‘", value, type.getName()));
		}
		return (T) value;
	}

	/**
	 * 获取指定key的缓存项，如果缓存项不存在，则通过 {@code valueLoader} 获取值
	 *
	 * @param key 缓存Key
	 * @param valueLoader 缓存ValueLoader
	 * @return 缓存值
	 * @param <T> 泛型
	 */
	<T> T get(Object key, Callable<T> valueLoader);

	/**
	 * 设置指定key的缓存项
	 *
	 * @param key 缓存Key
	 * @param value 缓存值
	 */
	void put(Object key, Object value);

	/**
	 * 设置指定key的缓存项，如果已存在则忽略
	 *
	 * @param key 缓存Key
	 * @param value 缓存值
	 * @return 缓存值
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
	 * @param key 存Key
	 */
	void evict(Object key);

	/**
	 * 删除指定的缓存项，如果不存在则返回 false
	 *
	 * @param key 缓存Key
	 * @return 删除是否成功
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
	 * @return 清空缓存是否成功
	 */
	default boolean invalidate() {
		clear();
		return false;
	}

	/**
	 * 获取缓存实例ID
	 *
	 * @return 缓存实例ID
	 */
	String getInstanceId();

	/**
	 * 获取缓存类型
	 *
	 * @return 缓存类型
	 */
	String getCacheType();

	/**
	 * 判断是否允许存储空值，避免缓存击穿
	 *
	 * @return 是否允许存储空值
	 */
	boolean isAllowNullValues();

	/**
	 * 获取 NULL值的过期时间（秒）
	 *
	 * @return NULL值的过期时间（秒）
	 */
	long getNullValueExpireTimeSeconds();

	/**
	 * 从存储值解析为具体值
	 *
	 * @param storeValue 存储值
	 * @return 具体值
	 */
	default Object fromStoreValue(Object storeValue) {
		return NullValueUtils.fromStoreValue(storeValue, this.isAllowNullValues());
	}

	/**
	 * 转换为存储值
	 *
	 * @param userValue 具体值
	 * @return 存储值
	 */
	default Object toStoreValue(Object userValue) {
		return NullValueUtils.toStoreValue(userValue, this.isAllowNullValues(), this.getCacheName());
	}

	default <K, V> Map<K, V> batchGet(Map<K, Object> keyMap, boolean isAllowNullValues) {
		return new HashMap<>();
	}

	default <K, V> Map<K, V> batchGet(Map<K, Object> keyMap) {
		return this.batchGet(keyMap, false);
	}

	default <K, V> Map<K, V> batchGet(List<K> keyList, Function<K, Object> cacheKeyBuilder) {
		Map<K, Object> keyMap = new HashMap<>();
		if (cacheKeyBuilder != null) {
			keyList.forEach(key -> keyMap.put(key, cacheKeyBuilder.apply(key)));
		} else {
			keyList.forEach(key -> keyMap.put(key, key));
		}
		return this.batchGet(keyMap);
	}

	default <K, V> Map<K, V> batchGet(List<K> keyList) {
		return this.batchGet(keyList, null);
	}

	default <K, V> Map<K, V> batchGetOrLoad(List<K> keyList, Function<K, Object> cacheKeyBuilder, Function<List<K>, Map<K, V>> valueLoader, boolean isAllowNullValues) {
		if (CollectionUtils.isEmpty(keyList)) {
			return Collections.emptyMap();
		}

		Map<K, Object> keyMap = new HashMap<>();
		if (cacheKeyBuilder != null) {
			keyList.forEach(key -> keyMap.put(key, cacheKeyBuilder.apply(key)));
		} else {
			keyList.forEach(key -> keyMap.put(key, key));
		}
		return this.batchGetOrLoad(keyMap, valueLoader, isAllowNullValues);
	}

	default <K, V> Map<K, V> batchGetOrLoad(Map<K, Object> keyMap, Function<List<K>, Map<K, V>> valueLoader, boolean isAllowNullValues) {
		return Collections.emptyMap();
	}

	default <K, V> Map<K, V> batchGetOrLoad(List<K> keyList, Function<K, Object> cacheKeyBuilder, Function<List<K>, Map<K, V>> valueLoader) {
		return this.batchGetOrLoad(keyList, cacheKeyBuilder, valueLoader, false);
	}

	default <K, V> Map<K, V> batchGetOrLoad(List<K> keyList, Function<List<K>, Map<K, V>> valueLoader) {
		return this.batchGetOrLoad(keyList, null, valueLoader, false);
	}

	default <K, V> void batchPut(Map<K, V> dataMap, Function<K, Object> cacheKeyBuilder) {
		if (CollectionUtils.isEmpty(dataMap)) {
			return;
		}
		Map<Object, V> dataMapTemp = new HashMap<>();
		if (cacheKeyBuilder != null) {
			dataMap.forEach((key, value) -> {
				dataMapTemp.put(cacheKeyBuilder.apply(key), value);
			});
		} else {
			dataMapTemp.putAll(dataMap);
		}
		this.batchPut(dataMapTemp);
	}

	default <V> void batchPut(Map<Object, V> dataMap) {
		if (CollectionUtils.isEmpty(dataMap)) {
			return;
		}
		dataMap.forEach(this::put);
	}
}
