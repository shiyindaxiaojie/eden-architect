package org.ylzl.eden.common.cache.strategy;

import org.ylzl.eden.spring.framework.extension.strategy.LoadingStrategy;

/**
 * 缓存扩展点加载策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CacheLoadingStrategy implements LoadingStrategy {

	public static final String META_INF_CACHE = "META-INF/cache/";

	@Override
	public String directory() {
		return META_INF_CACHE;
	}

	@Override
	public int getPriority() {
		return MAX_PRIORITY;
	}
}
