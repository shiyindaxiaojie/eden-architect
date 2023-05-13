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

package org.ylzl.eden.full.tracing.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.ylzl.eden.full.tracing.integration.redis.RedisShadowAspect;
import org.ylzl.eden.full.tracing.spring.boot.env.RedisShadowProperties;
import org.ylzl.eden.spring.data.redis.core.DynamicRedisTemplate;
import org.ylzl.eden.spring.data.redis.core.DynamicStringRedisTemplate;

/**
 * Redis 压测标记自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(value = RedisStressAutoConfiguration.ENABLED, havingValue = "true")
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisShadowProperties.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class RedisStressAutoConfiguration {

	public static final String ENABLED = "stress.redis.enabled";

	@Primary
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	@Bean(name = "redisTemplate")
	public DynamicRedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		DynamicRedisTemplate<Object, Object> redisTemplate = new DynamicRedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

	@Primary
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	@Bean(name = "stringRedisTemplate")
	public DynamicStringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		DynamicStringRedisTemplate redisTemplate = new DynamicStringRedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

	@Bean
	public RedisShadowAspect redisShadowAspect(RedisShadowProperties redisShadowProperties) {
		return new RedisShadowAspect(redisShadowProperties.getDatabase());
	}
}
