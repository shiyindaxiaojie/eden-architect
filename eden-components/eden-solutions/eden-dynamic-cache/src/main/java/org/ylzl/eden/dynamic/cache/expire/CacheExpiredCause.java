package org.ylzl.eden.dynamic.cache.expire;

/**
 * 缓存失效原因
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public enum CacheExpiredCause {

	EXPLICIT,
	REPLACED,
	COLLECTED,
	EXPIRED,
	SIZE;

	public static CacheExpiredCause parse(String type) {
		for (CacheExpiredCause cause : CacheExpiredCause.values()) {
			if (cause.name().equalsIgnoreCase(type)) {
				return cause;
			}
		}
		return null;
	}
}
