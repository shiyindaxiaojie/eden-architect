package org.ylzl.eden.trace.testing.integration.redis.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.ylzl.eden.spring.data.redis.core.DynamicRedisTemplate;
import org.ylzl.eden.spring.data.redis.core.DynamicStringRedisTemplate;
import org.ylzl.eden.trace.testing.env.RedisShadowProperties;
import org.ylzl.eden.trace.testing.integration.redis.aop.RedisShadowAspect;

/**
 * Redis 影子库自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisShadowProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RedisShadowAutoConfiguration  {

	@Primary
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	@Bean(name = "redisTemplate")
	public DynamicRedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		DynamicRedisTemplate<Object, Object> redisTemplate = new DynamicRedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

	@Primary
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	@Bean(name = "stringRedisTemplate")
	public DynamicStringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		DynamicStringRedisTemplate redisTemplate = new DynamicStringRedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

	@Bean
	public RedisShadowAspect redisShadowAspect(RedisShadowProperties redisShadowProperties) {
		return new RedisShadowAspect(redisShadowProperties);
	}
}
