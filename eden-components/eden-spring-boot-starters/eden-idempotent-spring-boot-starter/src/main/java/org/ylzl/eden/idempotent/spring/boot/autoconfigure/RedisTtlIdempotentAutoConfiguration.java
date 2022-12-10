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

package org.ylzl.eden.idempotent.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ylzl.eden.idempotent.integration.ttl.RedisTtlIdempotentStrategy;
import org.ylzl.eden.idempotent.spring.boot.env.IdempotentTtlConvertor;
import org.ylzl.eden.idempotent.spring.boot.env.IdempotentTtlProperties;
import org.ylzl.eden.idempotent.strategy.TtlIdempotentStrategy;

/**
 * 幂等请求自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnBean(StringRedisTemplate.class)
@ConditionalOnProperty(value = RedisTtlIdempotentAutoConfiguration.ENABLED, havingValue = "true")
@RequiredArgsConstructor
@Import(IdempotentAspectRegistrar.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RedisTtlIdempotentAutoConfiguration {

	public static final String ENABLED = IdempotentTtlProperties.PREFIX + ".redis.enabled";

	private static final String AUTOWIRED_REDIS_TTL_IDEMPOTENT_STRATEGY = "Autowired RedisTtlIdempotentStrategy";

	private final IdempotentTtlProperties properties;

	@Bean
	public TtlIdempotentStrategy ttlIdempotentStrategy(StringRedisTemplate redisTemplate) {
		log.debug(AUTOWIRED_REDIS_TTL_IDEMPOTENT_STRATEGY);
		return new RedisTtlIdempotentStrategy(redisTemplate, IdempotentTtlConvertor.INSTANCE.toConfig(properties));
	}
}
