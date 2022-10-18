package org.ylzl.eden.cache.adapter.cache;

/**
 * 缓存类型
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
public enum CacheType {

	NONE,
	COMPOSITE,
	CAFFEINE,
	GUAVA,
	REDIS;

	public static CacheType toCacheType(String type) {
		for (CacheType cacheType : CacheType.values()) {
			if (cacheType.name().equalsIgnoreCase(type)) {
				return cacheType;
			}
		}
		return null;
	}
}
