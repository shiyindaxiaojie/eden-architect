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

package org.ylzl.eden.spring.data.redis.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;

import java.lang.reflect.Method;

/**
 * Redis 缓存自动装配
 *
 * <p>变更日志：Spring Boot 升级 1.X 到 2.X
 *
 * <ul>
 *   <li>{@link RedisCacheManager} 移除构造器参数 {@link org.springframework.data.redis.core.RedisTemplate}
 *   <li>{@link RedisCacheManager} 使用 {@code create()} 创建实例
 * </ul>
 *
 * @author gyl
 * @since 2.0.0
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass({RedisOperations.class, RedisCacheManager.class})
@EnableCaching
@Slf4j
@Configuration
public class RedisCacheAutoConfiguration extends CachingConfigurerSupport {

	private static final String MSG_AUTOWIRED_REDIS_CACHE_MANAGER = "Autowired RedisCacheManager";

	private static final String BEAN_REDIS_CACHE_MGR = "redisCacheManager";

	private final RedisConnectionFactory redisConnectionFactory;

	public RedisCacheAutoConfiguration(RedisConnectionFactory redisConnectionFactory) {
		this.redisConnectionFactory = redisConnectionFactory;
	}

	@ConditionalOnMissingBean(name = BEAN_REDIS_CACHE_MGR)
	@Qualifier(BEAN_REDIS_CACHE_MGR)
	@Bean
	@Override
	public CacheManager cacheManager() {
		log.debug(MSG_AUTOWIRED_REDIS_CACHE_MANAGER);
		return RedisCacheManager.create(redisConnectionFactory);
	}

	@ConditionalOnMissingBean
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return new RedisKeyGenerator();
	}

	private static class RedisKeyGenerator implements KeyGenerator {

		@Override
		public Object generate(Object target, Method method, Object... params) {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(method.getName());
			for (Object obj : params) {
				sb.append(obj.toString());
			}
			return sb.toString();
		}
	}
}
