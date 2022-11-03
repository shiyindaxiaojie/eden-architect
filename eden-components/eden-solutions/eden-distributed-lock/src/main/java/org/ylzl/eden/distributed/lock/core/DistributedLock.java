package org.ylzl.eden.distributed.lock.core;

import org.ylzl.eden.distributed.lock.exception.DistributedLockException;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface DistributedLock {

	/**
	 * 加锁（阻塞）
	 *
	 * @param key 锁对象
	 */
	boolean lock(String key) throws DistributedLockException;

	/**
	 * 加锁（阻塞直到超时）
	 *
	 * @param key      锁对象
	 * @param waitTime 等待时间
	 * @param timeUnit 时间单位
	 * @return
	 * @throws DistributedLockException
	 */
	boolean lock(String key, int waitTime, TimeUnit timeUnit) throws DistributedLockException;

	/**
	 * 释放锁
	 *
	 * @param key 锁对象
	 * @throws DistributedLockException
	 */
	void unlock(String key) throws DistributedLockException;
}
