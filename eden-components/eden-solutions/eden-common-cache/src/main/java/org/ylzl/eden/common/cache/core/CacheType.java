package org.ylzl.eden.common.cache.core;

/**
 * 缓存类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 1.0.0
 */
public enum CacheType {

	NONE,
	COMPOSITE,
	CAFFEINE,
	GUAVA,
	EHCACHE,
	REDIS,
	MEMCACHED,
	DRAGONFLY;

	public static CacheType parse(String type) {
		for (CacheType cacheType : CacheType.values()) {
			if (cacheType.name().equalsIgnoreCase(type)) {
				return cacheType;
			}
		}
		return null;
	}
}
