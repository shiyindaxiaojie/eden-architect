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

package org.ylzl.eden.distributed.lock.integration.jedis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.distributed.lock.DistributedLock;
import org.ylzl.eden.distributed.lock.DistributedLockType;
import org.ylzl.eden.distributed.lock.exception.DistributedLockAcquireException;
import org.ylzl.eden.distributed.lock.exception.DistributedLockReleaseException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Jedis 分布式锁
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class JedisDistributedLock implements DistributedLock {

	private static final String UNLOCK_LUA =
		"if redis.call(\"get\",KEYS[1]) == ARGV[1] "
			+ "then return redis.call(\"del\",KEYS[1])"
			+ "else return 0 end";

	private final ThreadLocal<String> lock = new ThreadLocal<>();

	private final Jedis jedis;

	/**
	 * 锁类型
	 *
	 * @return 锁类型
	 */
	@Override
	public String lockType() {
		return DistributedLockType.JEDIS.name();
	}

	/**
	 * 阻塞加锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public boolean lock(@NonNull String key) {
		log.debug("Jedis create lock: {}", key);
		String value = UUID.randomUUID().toString();
		lock.set(value);
		SetParams setParams = new SetParams();
		setParams.ex(-1);
		try {
			String result = jedis.set(key, value, setParams);
			return StringUtils.isNotEmpty(result);
		} catch (Exception e) {
			log.error("Jedis create lock: {}, catch exception: {}", key, e.getMessage(), e);
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
		log.warn("Jedis create lock: {}, not support waitTime", key);
		return lock(key);
	}

	/**
	 * 释放锁
	 *
	 * @param key 锁对象
	 */
	@Override
	public void unlock(@NonNull String key) {
		log.debug("Jedis release lock: {}", key);
		try {
			final List<String> keys = Collections.singletonList(key);
			final List<String> args = Collections.singletonList(lock.get());
			Long result = (Long) jedis.eval(UNLOCK_LUA, keys, args);
			if (result == null || result == 0L) {
				log.warn("Jedis release lock: {}, but it not work", key);
			}
		} catch (Exception e) {
			log.error("Jedis release lock: {}, catch exception: {}", key, e.getMessage(), e);
			throw new DistributedLockReleaseException(e);
		}
	}
}
