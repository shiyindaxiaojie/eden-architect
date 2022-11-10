package org.ylzl.eden.idempotent.strategy;

import java.util.concurrent.TimeUnit;

/**
 * 基于令牌策略管理幂等请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface TokenIdempotentStrategy {

	/**
	 * 生成请求令牌
	 *
	 * @param ttl 存活时间
	 * @param timeUnit 时间单位
	 * @return 请求令牌
	 */
	String generateToken(long ttl, TimeUnit timeUnit);

	/**
	 * 校验请求令牌
	 *
	 * @param token 请求令牌
	 */
	void validateToken(String token);
}
