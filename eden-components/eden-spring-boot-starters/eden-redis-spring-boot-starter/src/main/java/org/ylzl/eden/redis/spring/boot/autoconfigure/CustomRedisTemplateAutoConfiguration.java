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

package org.ylzl.eden.redis.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ylzl.eden.spring.data.redis.core.CustomRedisTemplate;
import org.ylzl.eden.spring.data.redis.core.CustomRedisTemplateImpl;

/**
 * 自定义 Redis 模板自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnBean(StringRedisTemplate.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass({RedisOperations.class})
@Slf4j
@Configuration(proxyBeanMethods = false)
public class CustomRedisTemplateAutoConfiguration {

	public static final String AUTOWIRED_CUSTOM_REDIS_TEMPLATE = "Autowired CustomRedisTemplate";

	@ConditionalOnMissingBean
	@Bean
	public CustomRedisTemplate customRedisTemplate(StringRedisTemplate redisTemplate) {
		log.debug(AUTOWIRED_CUSTOM_REDIS_TEMPLATE);
		return new CustomRedisTemplateImpl(redisTemplate);
	}
}
