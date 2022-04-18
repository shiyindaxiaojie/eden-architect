package org.ylzl.eden.spring.integration.distributelock.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.ylzl.eden.spring.integration.distributelock.core.DistributedLock;
import org.ylzl.eden.spring.integration.distributelock.core.DistributedLockException;

import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁
 *
 * @author <a href="mailto:guoyuanlu@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class RedissonDistributedLock implements DistributedLock {

	private static final ThreadLocal<RLock> rLockThreadLocal = new ThreadLocal<>();

	private final RedissonClient redissonClient;

	/**
	 * 阻塞加锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public boolean lock(String key) throws DistributedLockException {
		log.debug("Redisson create lock: {}", key);
		RLock rLock = redissonClient.getFairLock(key);
		rLockThreadLocal.set(rLock);
		try {
			return rLock.tryLock();
		} catch (Exception e) {
			log.error("Redisson create lock: {}, catch exception: {}", key, e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 加锁
	 *
	 * @param key      锁对象
	 * @param waitTime 等待时间
	 * @param timeUnit 时间单位
	 * @throws DistributedLockException
	 * @return
	 */
	@Override
	public boolean lock(String key, int waitTime, TimeUnit timeUnit) throws DistributedLockException {
		log.debug("Redisson create lock: {}", key);
		RLock rLock = redissonClient.getFairLock(key);
		rLockThreadLocal.set(rLock);
		try {
			return rLock.tryLock(waitTime, 1, timeUnit);
		} catch (Exception e) {
			log.error("Redisson create lock: {}, waitTime: {}, catch exception: {}", key, e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 释放锁
	 *
	 * @param key 锁对象
	 * @throws DistributedLockException
	 */
	@Override
	public void unlock(String key) throws DistributedLockException {
		log.debug("Redisson release lock: {}", key);
		RLock rLock = rLockThreadLocal.get();
		if (rLock.isHeldByCurrentThread()) {
			try {
				rLock.unlock();
				rLockThreadLocal.remove();
			} catch (Exception e) {
				log.error("Redisson release lock: {}, catch exception: {}", key, e.getMessage(), e);
				throw new DistributedLockException(e.getMessage());
			}
		}
	}
}
