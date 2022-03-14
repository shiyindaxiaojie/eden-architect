package org.ylzl.eden.spring.integration.distributelock.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.ylzl.eden.spring.integration.distributelock.core.DistributeLock;

import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class RedissonDistributeLock implements DistributeLock {

	private final RLock rLock;

	/**
	 * 阻塞加锁
	 */
	@Override
	public boolean lock() throws Exception {
		return rLock.tryLock();
	}

	/**
	 * 加锁
	 *
	 * @param time     超时时间
	 * @param timeUnit 时间单位
	 */
	@Override
	public boolean lock(int time, TimeUnit timeUnit) throws Exception {
		return rLock.tryLock(time, 1, timeUnit);
	}

	/**
	 * 释放锁
	 */
	@Override
	public void unlock() {
		if (rLock.isHeldByCurrentThread()) {
			rLock.unlock();
		}
	}
}
