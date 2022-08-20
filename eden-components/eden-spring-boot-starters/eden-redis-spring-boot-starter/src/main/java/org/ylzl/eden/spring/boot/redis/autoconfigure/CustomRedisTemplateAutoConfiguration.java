package org.ylzl.eden.spring.boot.redis.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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
