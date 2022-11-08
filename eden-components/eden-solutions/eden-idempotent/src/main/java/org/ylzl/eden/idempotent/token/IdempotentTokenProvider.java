package org.ylzl.eden.idempotent.token;

import org.ylzl.eden.idempotent.model.IdempotentToken;

/**
 * 幂等令牌提供类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface IdempotentTokenProvider {

	/**
	 * 生成请求令牌
	 *
	 * @return 请求令牌
	 */
	IdempotentToken generate();

	/**
	 * 校验请求令牌
	 *
	 * @param idempotentToken 请求令牌
	 */
	void validate(IdempotentToken idempotentToken);
}
