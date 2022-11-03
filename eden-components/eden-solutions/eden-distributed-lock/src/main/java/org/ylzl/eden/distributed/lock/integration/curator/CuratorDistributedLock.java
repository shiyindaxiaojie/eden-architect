package org.ylzl.eden.distributed.lock.integration.curator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.exception.DistributedLockException;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * Curator 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class CuratorDistributedLock implements DistributedLock {

	private static final ThreadLocal<InterProcessMutex> interProcessMutexThreadLocal = new ThreadLocal<>();

	private final CuratorFramework curatorFramework;

	/**
	 * 阻塞加锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public boolean lock(@NotNull String key) throws DistributedLockException {
		log.debug("Curator create lock: {}", key);
		if (!key.startsWith(StringConstants.SLASH)) {
			throw new DistributedLockException("Invalid curator lock: " + key);
		}
		InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, key);
		try {
			interProcessMutex.acquire();
			interProcessMutexThreadLocal.set(interProcessMutex);
		} catch (Exception e) {
			log.error("Curator create lock: {}, catch exception: {}", key, e.getMessage(), e);
		}
		return true;
	}

	/**
	 * 加锁
	 *
	 * @param key      锁对象
	 * @param waitTime 等待时间
	 * @param timeUnit 时间单位
	 * @return
	 */
	@Override
	public boolean lock(@NotNull String key, int waitTime, TimeUnit timeUnit) throws DistributedLockException {
		log.debug("Curator create lock: {}, waitTime: {}", key, waitTime);
		if (!key.startsWith(StringConstants.SLASH)) {
			throw new DistributedLockException("Invalid curator lock: " + key);
		}
		InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, key);
		boolean isSuccess;
		try {
			isSuccess = interProcessMutex.acquire(waitTime, timeUnit);
		} catch (Exception e) {
			log.error("Curator create lock: {}, waitTime: {}, catch exception: {}", key, waitTime, e.getMessage(), e);
			throw new DistributedLockException(e.getMessage());
		}
		if (isSuccess) {
			interProcessMutexThreadLocal.set(interProcessMutex);
		}
		return isSuccess;
	}

	/**
	 * 释放锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public void unlock(String key) throws DistributedLockException {
		log.debug("Curator release lock: {}", key);
		InterProcessMutex interProcessMutex = interProcessMutexThreadLocal.get();
		if (interProcessMutex != null && interProcessMutex.isAcquiredInThisProcess()) {
			try {
				interProcessMutex.release();
				interProcessMutexThreadLocal.remove();
			} catch (Exception e) {
				log.error("Curator release lock: {}, catch exception: {}", key, e.getMessage(), e);
				throw new DistributedLockException(e.getMessage());
			}
		}
	}
}
