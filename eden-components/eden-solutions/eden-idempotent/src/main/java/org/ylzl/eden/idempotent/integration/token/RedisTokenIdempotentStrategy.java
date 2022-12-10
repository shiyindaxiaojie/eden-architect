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

package org.ylzl.eden.idempotent.integration.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ylzl.eden.commons.id.NanoIdUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.idempotent.config.IdempotentTokenConfig;
import org.ylzl.eden.idempotent.strategy.TokenIdempotentStrategy;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

/**
 * 基于 Redis 实现令牌策略管理幂等请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class RedisTokenIdempotentStrategy implements TokenIdempotentStrategy {

	private final StringRedisTemplate redisTemplate;

	private final IdempotentTokenConfig config;

	/**
	 * 生成请求令牌
	 *
	 * @return 请求令牌
	 */
	@Override
	public String generateToken() {
		String token = NanoIdUtils.randomNanoId();
		String key = this.buildKey(token);
		redisTemplate.opsForValue().set(key, Strings.EMPTY, config.getTtl(), config.getTimeUnit());
		return token;
	}

	/**
	 * 校验请求令牌
	 *
	 * @param token 请求令牌
	 */
	@Override
	public void validateToken(String token) {
		AssertUtils.notNull(token, "REQ-UNIQUE-401");

		// 如果不存在，表示已被其他请求处理，判定为重复请求
		String key = buildKey(token);
		AssertUtils.isTrue(Boolean.TRUE.equals(redisTemplate.hasKey(key)), "REQ-UNIQUE-403");

		// 如果删除失败，表示已被其他请求处理，判定为重复请求
		AssertUtils.isTrue(Boolean.TRUE.equals(redisTemplate.delete(key)), "REQ-UNIQUE-409");
	}

	/**
	 * 构建 Key
	 *
	 * @param token 请求令牌
	 * @return Key
	 */
	private String buildKey(String token) {
		return config.getPrefix() + ":" + token;
	}
}
