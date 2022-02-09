/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ylzl.eden.spring.data.redis.sequence;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Redis 序列提供类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class RedisSequenceProvider {

	private final RedisTemplate<String, Serializable> redisTemplate;

	public RedisSequenceProvider(RedisTemplate<String, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void set(String key, int value, Date expireTime) {
		RedisAtomicLong redisAtomicLong = getRedisAtomicLong(key);
		redisAtomicLong.set(value);
		redisAtomicLong.expireAt(expireTime);
	}

	public void set(String key, int value, long timeout, TimeUnit unit) {
		RedisAtomicLong redisAtomicLong = getRedisAtomicLong(key);
		redisAtomicLong.set(value);
		redisAtomicLong.expire(timeout, unit);
	}

	public long incrementAndGet(String key) {
		RedisAtomicLong redisAtomicLong = getRedisAtomicLong(key);
		return redisAtomicLong.incrementAndGet();
	}

	public long incrementAndGet(String key, Date expireTime) {
		RedisAtomicLong redisAtomicLong = getRedisAtomicLong(key);
		redisAtomicLong.expireAt(expireTime);
		return redisAtomicLong.incrementAndGet();
	}

	public long addAndGet(String key, int increment) {
		RedisAtomicLong redisAtomicLong = getRedisAtomicLong(key);
		return redisAtomicLong.addAndGet(increment);
	}

	public long addAndGet(String key, int increment, Date expireTime) {
		RedisAtomicLong redisAtomicLong = getRedisAtomicLong(key);
		redisAtomicLong.expireAt(expireTime);
		return redisAtomicLong.addAndGet(increment);
	}

	private RedisAtomicLong getRedisAtomicLong(String key) {
		return new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
	}
}
