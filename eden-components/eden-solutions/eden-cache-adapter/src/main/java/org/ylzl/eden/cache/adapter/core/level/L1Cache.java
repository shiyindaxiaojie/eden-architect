package org.ylzl.eden.cache.adapter.core.level;

import org.ylzl.eden.cache.adapter.core.Cache;
import org.ylzl.eden.cache.adapter.core.sync.CacheSynchronizer;

/**
 * 一级缓存接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface L1Cache extends Cache {

	CacheSynchronizer getCacheSynchronizer();
}
