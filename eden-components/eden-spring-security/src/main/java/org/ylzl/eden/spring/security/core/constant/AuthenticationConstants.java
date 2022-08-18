package org.ylzl.eden.spring.security.core.constant;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.StringConstants;

/**
 * 认证相关常量定义
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class AuthenticationConstants {

	/**
	 * Bearer 认证类型
	 */
	public static String BEARER_TYPE = "Bearer";

	/**
	 * Basic 认证类型
	 */
	public static String BASIC_TYPE = "Basic";

	/**
	 * Bearer 认证前缀
	 */
	public static final String BEARER_PREFIX = BEARER_TYPE + StringConstants.SPACE;

	/**
	 * Basic 认证前缀
	 */
	public static final String BASIC_PREFIX = BASIC_TYPE + StringConstants.SPACE;
}
