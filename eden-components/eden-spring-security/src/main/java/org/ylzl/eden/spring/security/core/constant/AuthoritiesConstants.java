package org.ylzl.eden.spring.security.core.constant;

import lombok.experimental.UtilityClass;

/**
 * 权限常量定义
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class AuthoritiesConstants {

	/**
	 * 管理员
	 */
	public static final String ADMIN = "ROLE_ADMIN";

	/**
	 * 注册用户
	 */
	public static final String USER = "ROLE_USER";

	/**
	 * 匿名用户
	 */
	public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
