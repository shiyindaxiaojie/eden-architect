package org.ylzl.eden.idempotent.strategy;

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
	 * @return 请求令牌
	 */
	String generateToken();

	/**
	 * 校验请求令牌
	 *
	 * @param token 请求令牌
	 */
	void validateToken(String token);
}
