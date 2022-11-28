package org.ylzl.eden.spring.security.jwt.token;

import org.ylzl.eden.spring.security.jwt.model.AccessToken;

/**
 * 令牌存储接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface JwtTokenStore {

	boolean validateAccessToken(AccessToken accessToken);

	void storeAccessToken(AccessToken accessToken);

	void removeAccessToken(AccessToken accessToken);
}
