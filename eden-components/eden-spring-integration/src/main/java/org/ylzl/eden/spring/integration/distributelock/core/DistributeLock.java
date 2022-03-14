package org.ylzl.eden.spring.integration.distributelock.core;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface DistributeLock {

	/**
	 * 阻塞加锁
	 */
	boolean lock() throws Exception;

	/**
	 * 加锁
	 * @param time 超时时间
	 * @param timeUnit 时间单位
	 */
	boolean lock(int time, TimeUnit timeUnit) throws Exception;

	/**
	 * 释放锁
	 */
	void unlock();
}
