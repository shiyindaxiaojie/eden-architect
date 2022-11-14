package org.ylzl.eden.idempotent.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ylzl.eden.idempotent.integration.token.RedisTokenIdempotentStrategy;
import org.ylzl.eden.idempotent.spring.boot.env.IdempotentTokenConvertor;
import org.ylzl.eden.idempotent.spring.boot.env.IdempotentTokenProperties;
import org.ylzl.eden.idempotent.strategy.TokenIdempotentStrategy;

/**
 * 幂等请求自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnBean(StringRedisTemplate.class)
@ConditionalOnProperty(IdempotentTokenProperties.PREFIX)
@RequiredArgsConstructor
@Import(IdempotentAspectRegistrar.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RedisTokenIdempotentAutoConfiguration {

	private static final String AUTOWIRED_REDIS_TOKEN_IDEMPOTENT_STRATEGY = "Autowired RedisTokenIdempotentStrategy";

	private final IdempotentTokenProperties properties;

	@Bean
	public TokenIdempotentStrategy tokenIdempotentStrategy(StringRedisTemplate redisTemplate) {
		log.debug(AUTOWIRED_REDIS_TOKEN_IDEMPOTENT_STRATEGY);
		return new RedisTokenIdempotentStrategy(redisTemplate, IdempotentTokenConvertor.INSTANCE.toConfig(properties));
	}
}
