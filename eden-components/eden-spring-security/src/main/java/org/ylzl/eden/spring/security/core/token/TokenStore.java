package org.ylzl.eden.spring.security.core.token;

/**
 * Jwt 令牌存储接口
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
public interface TokenStore {

	boolean validateAccessToken(AccessToken accessToken);

	void storeAccessToken(AccessToken accessToken);

	void removeAccessToken(AccessToken accessToken);
}
