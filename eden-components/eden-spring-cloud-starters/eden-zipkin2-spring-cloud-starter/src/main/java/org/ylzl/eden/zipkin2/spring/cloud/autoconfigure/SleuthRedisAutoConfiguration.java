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

package org.ylzl.eden.zipkin2.spring.cloud.autoconfigure;

import io.opentracing.Tracer;
import io.opentracing.contrib.redis.common.TracingConfiguration;
import io.opentracing.contrib.redis.redisson.TracingRedissonClient;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.redisson.spring.boot.autoconfigure.RedissonAutoConfiguration;
import org.ylzl.redisson.spring.boot.autoconfigure.util.RedissonUtils;
import org.ylzl.redisson.spring.boot.env.FixedRedissonProperties;

/**
 * Sleuth Redis 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnClass({RedissonClient.class, FixedRedissonProperties.class})
@ConditionalOnProperty(
	prefix = "spring.redis",
	value = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class SleuthRedisAutoConfiguration {

	public static final String AUTOWIRED_TRACING_REDISSON_CLIENT = "Autowired TracingRedissonClient";

	public static final String SERVER_ADDRESS = "Server Address";

	@ConditionalOnClass({RedissonClient.class, FixedRedissonProperties.class})
	@ConditionalOnBean({RedissonAutoConfiguration.class, FixedRedissonProperties.class})
	@Bean
	public RedissonClient tracingRedissonClient(Tracer tracer, RedisProperties redisProperties,
												FixedRedissonProperties fixedRedissonProperties) {
		log.debug(AUTOWIRED_TRACING_REDISSON_CLIENT);
		TracingConfiguration tracingConfiguration = new TracingConfiguration.Builder(tracer)
			// 拓展 Tag，设置 Redis 服务器地址
			.extensionTag(SERVER_ADDRESS, redisProperties.getHost() + ":" + redisProperties.getPort())
			.build();
		RedissonClient redissonClient = RedissonUtils.redissonClient(redisProperties, fixedRedissonProperties);
		return new TracingRedissonClient(redissonClient, tracingConfiguration);
	}

}
