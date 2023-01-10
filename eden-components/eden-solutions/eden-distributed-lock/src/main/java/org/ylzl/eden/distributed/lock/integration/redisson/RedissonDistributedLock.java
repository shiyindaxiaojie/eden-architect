/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.distributed.lock.integration.redisson;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.ylzl.eden.distributed.lock.DistributedLock;
import org.ylzl.eden.distributed.lock.DistributedLockType;
import org.ylzl.eden.distributed.lock.exception.DistributedLockAcquireException;
import org.ylzl.eden.distributed.lock.exception.DistributedLockReleaseException;
import org.ylzl.eden.distributed.lock.exception.DistributedLockTimeoutException;

import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class RedissonDistributedLock implements DistributedLock {

	private static final ThreadLocal<RLock> rLockThreadLocal = new ThreadLocal<>();

	private final RedissonClient redissonClient;

	/**
	 * 锁类型
	 *
	 * @return 锁类型
	 */
	@Override
	public String lockType() {
		return DistributedLockType.REDISSON.name();
	}

	/**
	 * 阻塞加锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public boolean lock(@NonNull String key) {
		log.debug("Redisson create lock: {}", key);
		RLock rLock = redissonClient.getFairLock(key);
		rLockThreadLocal.set(rLock);
		try {
			return rLock.tryLock();
		} catch (Exception e) {
			log.error("Redisson create lock: {}, catch exception: {}", key, e.getMessage(), e);
			throw new DistributedLockAcquireException(e);
		}
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
	public boolean lock(@NonNull String key, int waitTime, TimeUnit timeUnit) {
		log.debug("Redisson create lock: {}", key);
		RLock rLock = redissonClient.getFairLock(key);
		rLockThreadLocal.set(rLock);
		try {
			return rLock.tryLock(waitTime, 1, timeUnit);
		} catch (Exception e) {
			log.error("Redisson create lock: {}, waitTime: {}, catch exception: {}", key, e.getMessage(), e);
			throw new DistributedLockTimeoutException(e);
		}
	}

	/**
	 * 释放锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public void unlock(@NonNull String key) {
		log.debug("Redisson release lock: {}", key);
		RLock rLock = rLockThreadLocal.get();
		if (rLock.isHeldByCurrentThread()) {
			try {
				rLock.unlock();
				rLockThreadLocal.remove();
			} catch (Exception e) {
				log.error("Redisson release lock: {}, catch exception: {}", key, e.getMessage(), e);
				throw new DistributedLockReleaseException(e);
			}
		}
	}
}
