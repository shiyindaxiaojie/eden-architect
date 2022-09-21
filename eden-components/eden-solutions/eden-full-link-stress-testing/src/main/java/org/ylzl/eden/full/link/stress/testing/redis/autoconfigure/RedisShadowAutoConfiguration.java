package org.ylzl.eden.full.link.stress.testing.redis.autoconfigure;

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
import org.ylzl.eden.full.link.stress.testing.redis.core.SelectableRedisTemplate;
import org.ylzl.eden.full.link.stress.testing.redis.core.SelectableStringRedisTemplate;
import org.ylzl.eden.full.link.stress.testing.redis.env.RedisShadowProperties;

/**
 * TODO
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

	private final RedisShadowProperties redisShadowProperties;

	@Primary
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	@Bean(name = "redisTemplate")
	public SelectableRedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		SelectableRedisTemplate<Object, Object> redisTemplate = new SelectableRedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

	@Primary
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	@Bean(name = "stringRedisTemplate")
	public SelectableStringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		SelectableStringRedisTemplate redisTemplate = new SelectableStringRedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}
}
