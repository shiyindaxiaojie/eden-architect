package org.ylzl.eden.spring.integration.distributelock.core;

/**
 * 分布式锁管理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface DistributedLockManager {

	/**
	 * 获取分布式锁
	 *
	 * @param name 标识名称
	 * @return 分布式锁
	 */
	DistributeLock getLock(String name);
}
