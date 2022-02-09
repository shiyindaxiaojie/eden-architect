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

package org.ylzl.eden.spring.boot.redis.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.ylzl.eden.spring.data.redis.jedis.JedisTemplate;
import org.ylzl.eden.spring.data.redis.support.lock.DistributedRedisLock;
import org.ylzl.eden.spring.data.redis.support.lock.RedisLock;

/**
 * Redis 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass({RedisOperations.class})
@Slf4j
@Configuration
public class RedisClusterAutoConfiguration {

	@ConditionalOnMissingBean
	@Bean
	public JedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		JedisTemplate redisTemplate = new JedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

	@ConditionalOnMissingBean
	@Bean
	public RedisLock redisLock(RedisTemplate<String, Object> redisTemplate) {
		return new DistributedRedisLock(redisTemplate);
	}

	/*@ConditionalOnClass({Jedis.class, JedisCluster.class})
	@Configuration
	public static class FixedJedisAutoConfiguration {

		@ConditionalOnProperty(name = "spring.redis.cluster.nodes", matchIfMissing = false)
		@ConditionalOnMissingBean
		@Bean
		public FixedJedisCluster jedisCluster(RedisProperties redisProperties) {
			Set<HostAndPort> hostAndPorts = new HashSet<>();
			for (String node : redisProperties.getCluster().getNodes()) {
				String[] args = StringUtils.split(node, StringConstants.COMMA);
				hostAndPorts.add(new HostAndPort(args[0], Integer.valueOf(args[1]).intValue()));
			}
			int maxAttempts = 3;
			GenericObjectPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
			poolConfig.setMinIdle(redisProperties.getJedis().getPool().getMinIdle());
			poolConfig.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().toMillis());

			return new FixedJedisCluster(
				hostAndPorts,
				redisProperties.getTimeout().getNano() / 1_000_000,
				redisProperties.getTimeout().getNano() / 1_000_000,
				maxAttempts,
				redisProperties.getPassword(),
				poolConfig);
		}
	}*/
}
