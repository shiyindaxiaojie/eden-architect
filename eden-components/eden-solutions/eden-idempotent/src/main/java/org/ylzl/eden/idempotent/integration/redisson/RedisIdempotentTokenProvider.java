package org.ylzl.eden.idempotent.integration.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ylzl.eden.commons.id.NanoIdUtils;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.idempotent.model.IdempotentToken;
import org.ylzl.eden.idempotent.token.IdempotentTokenProvider;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.util.concurrent.TimeUnit;

/**
 * Redis 幂等令牌提供类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class RedisIdempotentTokenProvider implements IdempotentTokenProvider {

	private final StringRedisTemplate redisTemplate;

	private final String keyPrefix;

	/**
	 * 生成请求令牌
	 *
	 * @return 请求令牌
	 */
	@Override
	public IdempotentToken generate() {
		String token = NanoIdUtils.randomNanoId();
		String key = this.buildKey(token);
		redisTemplate.opsForValue().set(key, StringConstants.EMPTY, 60, TimeUnit.SECONDS);
		return IdempotentToken.builder().value(token).build();
	}

	/**
	 * 校验请求令牌
	 *
	 * @param idempotentToken 请求令牌
	 */
	@Override
	public void validate(IdempotentToken idempotentToken) {
		String token = idempotentToken.getValue();
		AssertUtils.notNull(token, "REQ-UNIQUE-401");

		// 如果不存在，表示已被其他请求处理，判定为重复请求
		String key = buildKey(token);
		AssertUtils.isTrue(Boolean.TRUE.equals(redisTemplate.hasKey(key)), "REQ-UNIQUE-403");

		// 如果删除失败，表示已被其他请求处理，判定为重复请求
		AssertUtils.isTrue(Boolean.TRUE.equals(redisTemplate.delete(key)), "REQ-UNIQUE-409");
	}

	private String buildKey(String uid) {
		return keyPrefix + ":" + uid;
	}
}
