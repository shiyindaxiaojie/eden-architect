package org.ylzl.eden.common.cache.loader;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.common.cache.level.L2Cache;
import org.ylzl.eden.common.cache.sync.CacheSynchronizer;

import java.util.function.Function;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class LoadFunction implements Function<Object, Object> {

	private final String instanceId;

	private final String cacheType;

	private final String cacheName;

	private final L2Cache l2Cache;

	private final CacheSynchronizer cacheSynchronizer;

	private final ValueLoaderWrapper valueLoader;

	private final boolean allowNullValues;

	private Cache<Object, Integer> nullValueCache;

	@Override
	public Object apply(Object key) {
		return null;
	}
}
