package org.ylzl.eden.spring.security.jwt.constant;

import io.jsonwebtoken.Claims;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.StringConstants;

/**
 * JWT 常量定义
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class JwtConstants {

	/**  Bearer 认证类型 */
	public static String BEARER_TYPE = "Bearer";

	/** Bearer 认证前缀 */
	public static final String BEARER_PREFIX = BEARER_TYPE + StringConstants.SPACE;

	public static final String AUDIENCE = Claims.AUDIENCE;

	public static final String EXPIRATION = Claims.EXPIRATION;

	public static final String ID = Claims.ID;

	public static final String ISSUED_AT = Claims.ISSUED_AT;

	public static final String ISSUER = Claims.ISSUER;

	public static final String NOT_BEFORE = Claims.NOT_BEFORE;

	public static final String SUBJECT = Claims.SUBJECT;

	public static final String AUTHORITIES_KEY = "authorities";
}
