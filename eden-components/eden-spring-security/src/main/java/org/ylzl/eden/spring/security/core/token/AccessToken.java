package org.ylzl.eden.spring.security.core.token;

import lombok.*;
import org.ylzl.eden.spring.security.core.constant.AuthenticationConstants;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 访问令牌
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class AccessToken {

	/**
	 * 访问令牌
	 */
	private String value;

	/**
	 * 刷新令牌
	 */
	private RefreshToken refreshToken;

	/**
	 * 过期时间
	 */
	private Date expiration;

	/**
	 * 授权类型
	 */
	private String tokenType = AuthenticationConstants.BEARER_TYPE.toLowerCase();

	/**
	 * 作用域
	 */
	private Set<String> scope;

	/**
	 * 附加信息
	 */
	private Map<String, Object> additionalInformation = Collections.emptyMap();

	public int getExpiresIn() {
		return expiration != null ? Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue() : 0;
	}

	public void setExpiresIn(int delta) {
		setExpiration(new Date(System.currentTimeMillis() + delta));
	}

	public boolean isExpired() {
		return expiration != null && expiration.before(new Date());
	}
}
