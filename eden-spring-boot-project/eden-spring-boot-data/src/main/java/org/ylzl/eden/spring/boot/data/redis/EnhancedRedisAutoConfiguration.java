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

package org.ylzl.eden.spring.boot.data.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;
import org.ylzl.eden.spring.boot.data.redis.jedis.FixedJedisCluster;
import org.ylzl.eden.spring.boot.data.redis.support.EnhancedRedisTemplate;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.StringUtils.split;

/**
 * Redis 自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass({RedisOperations.class})
@Slf4j
@Configuration
public class EnhancedRedisAutoConfiguration {

	@ConditionalOnClass({Jedis.class})
	public static class FixedJedisAutoConfiguration {

		@ConditionalOnMissingBean
		@Bean
		public FixedJedisCluster jedisCluster(RedisProperties redisProperties) {
			Set<HostAndPort> hostAndPorts = new HashSet<>();
			for (String node : redisProperties.getCluster().getNodes()) {
				String[] args = split(node, StringConstants.COMMA);
				hostAndPorts.add(new HostAndPort(args[0], Integer.valueOf(args[1]).intValue()));
			}
			int maxAttempts = 3;
			GenericObjectPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxIdle(redisProperties.getPool().getMaxIdle());
			poolConfig.setMinIdle(redisProperties.getPool().getMinIdle());
			poolConfig.setMaxWaitMillis(redisProperties.getPool().getMaxWait() * 1000L);

			return new FixedJedisCluster(hostAndPorts, redisProperties.getTimeout(), redisProperties.getTimeout(),
				maxAttempts, redisProperties.getPassword(), poolConfig);
		}
	}

	@ConditionalOnMissingBean
	@Bean
	public EnhancedRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		EnhancedRedisTemplate redisTemplate = new EnhancedRedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}
}
