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

package org.ylzl.eden.idempotent.integration.ttl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.idempotent.config.TimeToLiveIdempotentConfig;
import org.ylzl.eden.idempotent.strategy.TimeToLiveIdempotentStrategy;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 实现过期策略管理幂等请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class RedisTimeToLiveIdempotentStrategy implements TimeToLiveIdempotentStrategy {

	private final StringRedisTemplate redisTemplate;

	private final TimeToLiveIdempotentConfig config;

	/**
	 * 检查是否首次请求
	 *
	 * @param key      键
	 * @param ttl      存活时间
	 * @param timeUnit 时间单位
	 */
	@Override
	public void checkOnceRequest(String key, long ttl, TimeUnit timeUnit) {
		String resolveKey = config.getPrefix() + ":" + key;

		// 如果存在，表示已被其他请求处理，判定为重复请求
		AssertUtils.isTrue(Boolean.FALSE.equals(redisTemplate.hasKey(resolveKey)), "REQ-UNIQUE-409");

		// 如果存储失败，表示已被其他请求处理，判定为重复请求
		boolean isSuccess = Boolean.TRUE.equals(
			redisTemplate.opsForValue().setIfAbsent(resolveKey, Strings.EMPTY, ttl, timeUnit));
		AssertUtils.isTrue(isSuccess, "REQ-UNIQUE-409");
	}
}
