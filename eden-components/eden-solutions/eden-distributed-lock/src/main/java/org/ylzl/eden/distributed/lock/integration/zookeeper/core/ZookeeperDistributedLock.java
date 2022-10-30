package org.ylzl.eden.distributed.lock.integration.zookeeper.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.distributed.lock.core.DistributedLock;
import org.ylzl.eden.distributed.lock.core.DistributedLockException;
import org.ylzl.eden.spring.cloud.zookeeper.core.ZookeeperTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Zookeeper 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Deprecated
@RequiredArgsConstructor
@Slf4j
public class ZookeeperDistributedLock implements DistributedLock {

	private static final byte[] EMPTY_DATA = new byte[0];

	private final ZookeeperTemplate zooKeeperTemplate;

	/**
	 * 阻塞加锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public boolean lock(String key) throws DistributedLockException {
		log.debug("Zookeeper create lock: {}", key);
		try {
			zooKeeperTemplate.create(key, EMPTY_DATA);
		} catch (Exception e) {
			log.error("Zookeeper create lock: {}, catch exception: {}", key, e.getMessage(), e);
			return false;
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
	 * @throws DistributedLockException
	 */
	@Override
	public boolean lock(String key, int waitTime, TimeUnit timeUnit) throws DistributedLockException {
		log.warn("Zookeeper create lock: {}, not support waitTime", key);
		return lock(key);
	}

	/**
	 * 释放锁
	 *
	 * @param key 锁对象
	 * @throws DistributedLockException
	 */
	@Override
	public void unlock(String key) throws DistributedLockException {
		log.debug("Zookeeper release lock: {}", key);
		try {
			zooKeeperTemplate.delete(key);
		} catch (Exception e) {
			log.error("Zookeeper release lock: {}, catch exception: {}", key, e.getMessage(), e);
			throw new DistributedLockException(e.getMessage());
		}
	}
}
