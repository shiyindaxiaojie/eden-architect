package org.ylzl.eden.dynamic.cache.sync;

import org.ylzl.eden.extension.SPI;

/**
 * 缓存同步器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI
public interface CacheSynchronizer {

	/**
	 * 建立连接，订阅消息
	 */
	void connnect();
}
