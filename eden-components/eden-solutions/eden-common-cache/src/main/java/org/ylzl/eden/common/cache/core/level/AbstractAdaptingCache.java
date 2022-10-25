package org.ylzl.eden.common.cache.core.level;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.common.cache.core.Cache;
import org.ylzl.eden.common.cache.core.config.CacheConfig;

/**
 * 缓存接口适配器
 *
 * @see org.springframework.cache.support.AbstractValueAdaptingCache
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
}
