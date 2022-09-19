package org.ylzl.spring.boot.redisson.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.ylzl.spring.boot.redisson.autoconfigure.util.RedissonUtils;
import org.ylzl.spring.boot.redisson.env.FixedRedissonProperties;

/**
 * Redisson 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnProperty(value = "redisson.enabled", matchIfMissing = true)
@EnableConfigurationProperties(FixedRedissonProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RedissonAutoConfiguration {

	private final RedisProperties redisProperties;

	private final FixedRedissonProperties redissonProperties;

	@Bean
	@Lazy
	@ConditionalOnMissingBean(RedissonReactiveClient.class)
	public RedissonReactiveClient redissonReactive(RedissonClient redisson) {
		return redisson.reactive();
	}

	@Bean
	@Lazy
	@ConditionalOnMissingBean(RedissonRxClient.class)
	public RedissonRxClient redissonRxJava(RedissonClient redisson) {
		return redisson.rxJava();
	}

	@Bean
	@ConditionalOnMissingBean(RedisConnectionFactory.class)
	public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
		return new RedissonConnectionFactory(redisson);
	}

	@ConditionalOnMissingBean(RedissonClient.class)
	@Bean(destroyMethod = "shutdown")
	public RedissonClient redissonClient() {
		return RedissonUtils.redissonClient(redisProperties, redissonProperties);
	}
}
