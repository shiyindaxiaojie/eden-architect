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

package org.ylzl.eden.distributed.lock.integration.zookeeper;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.distributed.lock.DistributedLock;
import org.ylzl.eden.distributed.lock.DistributedLockType;
import org.ylzl.eden.distributed.lock.exception.DistributedLockAcquireException;
import org.ylzl.eden.distributed.lock.exception.DistributedLockReleaseException;

import java.util.concurrent.TimeUnit;

/**
 * Zookeeper 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class ZookeeperDistributedLock implements DistributedLock {

	private static final TransmittableThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();

	private static final byte[] EMPTY_DATA = new byte[0];

	private final ZooKeeper zooKeeper;

	/**
	 * 锁类型
	 *
	 * @return 锁类型
	 */
	@Override
	public String lockType() {
		return DistributedLockType.ZOOKEEPER.name();
	}

	/**
	 * 阻塞加锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public boolean lock(@NonNull String key) {
		log.debug("Zookeeper create lock '{}'", key);
		boolean isSuccess;
		String result;
		try {
			result = zooKeeper.create(key, EMPTY_DATA, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			isSuccess = StringUtils.isNotEmpty(result);
		} catch (Exception e) {
			log.error("Zookeeper create lock '{}', catch exception: {}", key, e.getMessage(), e);
			throw new DistributedLockAcquireException(e);
		}
		if (isSuccess) {
			threadLocal.set(result);
			log.debug("Zookeeper create lock '{}' successfully", key);
		} else {
			log.warn("Zookeeper create lock '{}' failed", key);
		}
		return isSuccess;
	}

	/**
	 * 加锁
	 *
	 * @param key      锁对象
	 * @param waitTime 等待时间
	 * @param timeUnit 时间单位
	 * @return 加锁是否成功
	 */
	@Override
	public boolean lock(@NonNull String key, int waitTime, TimeUnit timeUnit) {
		log.warn("Zookeeper create lock '{}' not support waitTime", key);
		return lock(key);
	}

	/**
	 * 释放锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public void unlock(@NonNull String key) {
		log.debug("Zookeeper release lock '{}'", key);
		String result = threadLocal.get();
		if (result == null) {
			log.warn("Zookeeper release lock '{}' failed due to thread local is null", key);
			return;
		}
		try {
			zooKeeper.delete(key, -1);
		} catch (Exception e) {
			log.error("Zookeeper release lock '{}', catch exception: {}", key, e.getMessage(), e);
			throw new DistributedLockReleaseException(e);
		}
		log.debug("Zookeeper release lock '{}' successfully", key);
	}
}
