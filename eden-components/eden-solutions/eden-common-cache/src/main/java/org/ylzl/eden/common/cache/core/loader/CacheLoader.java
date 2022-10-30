package org.ylzl.eden.common.cache.core.loader;

import org.ylzl.eden.common.cache.core.config.CacheSpec;
import org.ylzl.eden.common.cache.core.level.L2Cache;
import org.ylzl.eden.common.cache.core.sync.CacheSynchronizer;
import org.ylzl.eden.spring.framework.extension.SPI;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * 缓存加载器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI
public interface CacheLoader<K, V> extends Serializable {

	V load(K key);

	void addValueLoader(Object key, Callable<?> valueLoader);

	void removeValueLoader(Object key);

	void setL2Cache(L2Cache l2Cache);

	void setCacheSpec(CacheSpec cacheSpec);

	void setCacheSynchronizer(CacheSynchronizer cacheSynchronizer);

	void setAllowNullValues(boolean allowNullValues);
}
