package org.ylzl.eden.spring.security.core.token;

/**
 * Jwt 令牌存储接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface TokenStore {

	boolean validateAccessToken(AccessToken accessToken);

	void storeAccessToken(AccessToken accessToken);

	void removeAccessToken(AccessToken accessToken);
}
