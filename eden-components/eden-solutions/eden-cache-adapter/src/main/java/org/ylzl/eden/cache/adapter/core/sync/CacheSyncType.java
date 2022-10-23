package org.ylzl.eden.cache.adapter.core.sync;

/**
 * 缓存同步类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public enum CacheSyncType {

	REDIS,
	KAFKA,
	ROCKETMQ;

	public static CacheSyncType toCacheSyncType(String type) {
		for (CacheSyncType cacheSyncType : CacheSyncType.values()) {
			if (cacheSyncType.name().equalsIgnoreCase(type)) {
				return cacheSyncType;
			}
		}
		return null;
	}
}
