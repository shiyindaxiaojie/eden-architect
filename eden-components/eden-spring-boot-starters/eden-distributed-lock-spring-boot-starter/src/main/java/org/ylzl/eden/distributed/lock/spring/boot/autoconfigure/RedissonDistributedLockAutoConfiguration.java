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

package org.ylzl.eden.distributed.lock.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.lock.DistributedLock;
import org.ylzl.eden.distributed.lock.integration.redisson.RedissonDistributedLock;
import org.ylzl.eden.distributed.lock.spring.boot.env.DistributedLockProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * Redisson 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = DistributedLockProperties.Redisson.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@AutoConfigureBefore(DistributedLockAutoConfiguration.class)
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@ConditionalOnClass(RedissonClient.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RedissonDistributedLockAutoConfiguration {

	public static final String AUTOWIRED_REDISSON_DISTRIBUTED_LOCK = "Autowired RedissonDistributedLock";

	@ConditionalOnClass(Redisson.class)
	@ConditionalOnBean(RedissonClient.class)
	@Bean
	public DistributedLock distributedLock(RedissonClient redissonClient) {
		log.debug(AUTOWIRED_REDISSON_DISTRIBUTED_LOCK);
		return new RedissonDistributedLock(redissonClient);
	}
}
