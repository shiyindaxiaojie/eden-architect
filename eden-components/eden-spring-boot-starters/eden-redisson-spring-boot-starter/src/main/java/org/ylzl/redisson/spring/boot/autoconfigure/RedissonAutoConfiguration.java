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

package org.ylzl.redisson.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.redisson.spring.boot.autoconfigure.util.RedissonUtils;
import org.ylzl.redisson.spring.boot.env.FixedRedissonProperties;

/**
 * Redisson 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = FixedRedissonProperties.PREFIX,
	name = Conditions.ENABLED,
	value = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnClass(RedissonClient.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(FixedRedissonProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RedissonAutoConfiguration {

	private final RedisProperties redisProperties;

	private final FixedRedissonProperties redissonProperties;

	@Bean
	@Lazy
	@ConditionalOnMissingBean(RedissonReactiveClient.class)
	public RedissonReactiveClient redissonReactive(RedissonClient redisson) {
		return redisson.reactive();
	}

	@Bean
	@Lazy
	@ConditionalOnMissingBean(RedissonRxClient.class)
	public RedissonRxClient redissonRxJava(RedissonClient redisson) {
		return redisson.rxJava();
	}

	@Bean
	@ConditionalOnMissingBean(RedisConnectionFactory.class)
	public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
		return new RedissonConnectionFactory(redisson);
	}

	@ConditionalOnMissingBean(RedissonClient.class)
	@Bean(destroyMethod = "shutdown")
	public RedissonClient redissonClient() {
		return RedissonUtils.redissonClient(redisProperties, redissonProperties);
	}
}
