package org.ylzl.eden.common.cache.sync;

import org.ylzl.eden.common.cache.model.CacheInfo;

/**
 * 缓存同步监听器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface CacheSyncListener {

	/**
	 * 消息处理
	 *
	 * @param info 缓存信息
	 */
	void onMessage(CacheInfo info);
}
