package org.ylzl.eden.common.cache.sync;

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

	public static CacheSyncType parse(String type) {
		for (CacheSyncType cacheSyncType : CacheSyncType.values()) {
			if (cacheSyncType.name().equalsIgnoreCase(type)) {
				return cacheSyncType;
			}
		}
		return null;
	}
}
