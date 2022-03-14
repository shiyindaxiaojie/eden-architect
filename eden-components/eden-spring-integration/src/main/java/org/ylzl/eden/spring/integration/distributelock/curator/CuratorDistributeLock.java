package org.ylzl.eden.spring.integration.distributelock.curator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.ylzl.eden.spring.integration.distributelock.core.DistributeLock;

import java.util.concurrent.TimeUnit;

/**
 * Curator 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class CuratorDistributeLock implements DistributeLock {

	private final InterProcessMutex interProcessMutex;

	/**
	 * 阻塞加锁
	 */
	@Override
	public boolean lock() throws Exception {
		interProcessMutex.acquire();
		return true;
	}

	/**
	 * 加锁
	 *
	 * @param time     超时时间
	 * @param timeUnit 时间单位
	 */
	@Override
	public boolean lock(int time, TimeUnit timeUnit) throws Exception {
		return interProcessMutex.acquire(time, timeUnit);
	}

	/**
	 * 释放锁
	 */
	@Override
	public void unlock() {
		try {
			if (interProcessMutex.isAcquiredInThisProcess()) {
				interProcessMutex.release();
			}
		} catch (Exception e) {
			log.error("Distributed lock unlock failed. ", e);
		}
	}
}
