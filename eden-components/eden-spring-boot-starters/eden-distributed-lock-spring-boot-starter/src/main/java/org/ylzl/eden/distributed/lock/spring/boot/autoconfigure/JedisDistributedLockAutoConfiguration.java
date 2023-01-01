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
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.ylzl.eden.distributed.lock.DistributedLock;
import org.ylzl.eden.distributed.lock.integration.jedis.JedisDistributedLock;
import org.ylzl.eden.distributed.lock.spring.boot.env.DistributedLockProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import redis.clients.jedis.Jedis;

/**
 * Jedis 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = DistributedLockProperties.Jedis.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE
)
@AutoConfigureBefore(DistributedLockAutoConfiguration.class)
@ConditionalOnClass(Jedis.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class JedisDistributedLockAutoConfiguration {

	public static final String AUTOWIRED_JEDIS_DISTRIBUTED_LOCK = "Autowired JedisDistributedLock";

	@Bean
	public DistributedLock distributedLock(RedisTemplate<String, Object> redisTemplate) {
		log.debug(AUTOWIRED_JEDIS_DISTRIBUTED_LOCK);
		return new JedisDistributedLock(redisTemplate);
	}
}
