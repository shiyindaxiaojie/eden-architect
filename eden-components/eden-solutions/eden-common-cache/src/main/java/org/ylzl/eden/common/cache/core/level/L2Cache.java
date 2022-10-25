package org.ylzl.eden.common.cache.core.level;

import org.ylzl.eden.common.cache.core.Cache;

/**
 * 二级缓存接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface L2Cache extends Cache {

	/**
	 * 构建key
	 */
	Object buildKey(Object key);

	/**
	 * 获取缓存过期实践
	 */
	long getExpireTime();
}
