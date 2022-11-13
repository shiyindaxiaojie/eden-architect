package org.ylzl.eden.idempotent.integration.ttl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ylzl.eden.idempotent.config.IdempotentTtlConfig;
import org.ylzl.eden.idempotent.strategy.TtlIdempotentStrategy;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 实现过期策略管理幂等请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class RedisTtlIdempotentStrategy implements TtlIdempotentStrategy {

	private final StringRedisTemplate redisTemplate;

	private final IdempotentTtlConfig config;

	/**
	 * 检查是否首次请求
	 *
	 * @param key      键
	 * @param value    值
	 * @param ttl      存活时间
	 * @param timeUnit 时间单位
	 */
	@Override
	public void checkFirstRequest(String key, String value, long ttl, TimeUnit timeUnit) {
		String resolveKey = config.getPrefix() + ":" + key;

		// 如果存在，表示已被其他请求处理，判定为重复请求
		AssertUtils.isTrue(Boolean.FALSE.equals(redisTemplate.hasKey(resolveKey)), "REQ-UNIQUE-409");

		// 如果存储失败，表示已被其他请求处理，判定为重复请求
		boolean isSuccess = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(resolveKey, value, ttl, timeUnit));
		AssertUtils.isTrue(isSuccess, "REQ-UNIQUE-409");
	}
}
