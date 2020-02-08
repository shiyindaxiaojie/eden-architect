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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.ylzl.eden.spring.boot.data.redis.serializer.IntegerRedisSerializer;
import org.ylzl.eden.spring.boot.data.redis.serializer.LongRedisSerializer;

import java.lang.reflect.Method;

/**
 * Redis 缓存自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass({RedisOperations.class, RedisCacheManager.class})
@EnableCaching
@Slf4j
@Configuration
public class RedisCacheAutoConfiguration extends CachingConfigurerSupport {

    private static final String BEAN_REDIS_CACHE_MGR = "redisCacheManager";

    private static final String BEAN_INTEGER_REDIS_CACHE_MGR = "integerRedisCacheManager";

    private static final String BEAN_LONG_REDIS_CACHE_MGR = "longRedisCacheManager";

    private static final String BEAN_STRING_REDIS_CACHE_MGR = "stringRedisCacheManager";

    private final RedisConnectionFactory redisConnectionFactory;

    private final RedisTemplate redisTemplate;

    public RedisCacheAutoConfiguration(RedisConnectionFactory redisConnectionFactory, RedisTemplate redisTemplate) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.redisTemplate = redisTemplate;
    }

    @ConditionalOnMissingBean(name = BEAN_REDIS_CACHE_MGR)
    @Qualifier(BEAN_REDIS_CACHE_MGR)
    @Bean
    @Override
    public CacheManager cacheManager() {
        return new RedisCacheManager(redisTemplate);
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new RedisKeyGenerator();
    }

    @ConditionalOnMissingBean(name = BEAN_STRING_REDIS_CACHE_MGR)
    @Bean
    public RedisCacheManager stringRedisCacheManager(StringRedisTemplate stringRedisTemplate) {
        return new RedisCacheManager(stringRedisTemplate);
    }

    @SuppressWarnings("unchecked")
    @ConditionalOnMissingBean(name = BEAN_INTEGER_REDIS_CACHE_MGR)
    @Bean
    public RedisCacheManager integerRedisCacheManager() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(IntegerRedisSerializer.INSTANCE);
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return new RedisCacheManager(redisTemplate);
    }

    @SuppressWarnings("unchecked")
    @ConditionalOnMissingBean(name = BEAN_LONG_REDIS_CACHE_MGR)
    @Bean
    public RedisCacheManager longRedisCacheManager() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(LongRedisSerializer.INSTANCE);
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return new RedisCacheManager(redisTemplate);
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
