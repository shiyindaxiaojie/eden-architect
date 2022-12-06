package org.ylzl.eden.dynamic.cache.level;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.core.Cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 缓存接口适配器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public abstract class AbstractAdaptingCache implements Cache {

	private final String cacheName;

	private final CacheConfig cacheConfig;

	@Override
	public String getCacheName() {
		return this.cacheName;
	}

	@Override
	public String getInstanceId() {
		return cacheConfig.getInstanceId();
	}

	@Override
	public boolean isAllowNullValues() {
		return cacheConfig.isAllowNullValues();
	}

	@Override
	public long getNullValueExpireTimeSeconds() {
		return cacheConfig.getNullValueExpireInSeconds();
	}

	@Override
	public <K, V> Map<K, V> batchGetOrLoad(Map<K, Object> keyMap, Function<List<K>, Map<K, V>> valueLoader,
										   boolean isAllowNullValues) {
		return Cache.super.batchGetOrLoad(keyMap, valueLoader, isAllowNullValues);
	}

	protected <K, V> Map<K, V> filterNullValue(Map<K, V> hitCacheMap, boolean isAllowNullValues) {
		if (isAllowNullValues) {
			return hitCacheMap;
		}
		return hitCacheMap.entrySet().stream()
			.filter(entry -> entry.getValue() != null)
			.collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
	}

	protected <K, V> Map<K, V> loadAndPut(Function<List<K>, Map<K, V>> valueLoader, Map<K, Object> notHitCacheKeyMap) {
		Map<K, V> valueLoaderHitMap = valueLoader.apply(new ArrayList<>(notHitCacheKeyMap.keySet()));
		if (CollectionUtils.isEmpty(valueLoaderHitMap)) {
			Map<Object, V> nullValueMap = new HashMap<>();
			notHitCacheKeyMap.forEach((k, cacheKey) -> {
				nullValueMap.put(cacheKey, null);
			});
			this.batchPut(nullValueMap);
			return valueLoaderHitMap;
		}

		Map<Object, V> batchPutDataMap = notHitCacheKeyMap.entrySet().stream()
			.filter(entry -> valueLoaderHitMap.containsKey(entry.getKey()))
			.collect(HashMap::new, (map, entry) -> map.put(entry.getValue(),
				valueLoaderHitMap.get(entry.getKey())), HashMap::putAll);
		this.batchPut(batchPutDataMap);

		if (valueLoaderHitMap.size() != notHitCacheKeyMap.size()) {
			Map<Object, V> nullValueMap = new HashMap<>();
			notHitCacheKeyMap.forEach((k, cacheKey) -> {
				if (!valueLoaderHitMap.containsKey(k)) {
					nullValueMap.put(cacheKey, null);
				}
			});
			this.batchPut(nullValueMap);
		}
		return valueLoaderHitMap;
	}
}
