package org.ylzl.eden.common.security.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 认证工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class SecurityUtils {

	/** 匿名用户 */
	public static final String ANONYMOUS = "ROLE_ANONYMOUS";

	/**
	 * 获取登录用户
	 */
	public static Optional<String> getPrincipal() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
	}

	/**
	 * 获取登录用户
	 */
	public static Optional<String> getUserName() {
		return getPrincipal();
	}

	/**
	 * 获取登录凭据
	 */
	public static Optional<String> getCredentials() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional
			.ofNullable(securityContext.getAuthentication())
			.filter(authentication -> authentication.getCredentials() instanceof String)
			.map(authentication -> (String) authentication.getCredentials());
	}

	/**
	 * 是否已认证
	 *
	 * @return
	 */
	public static boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && getAuthorities(authentication).noneMatch(ANONYMOUS::equals);
	}

	/**
	 * 获取认证信息
	 *
	 * @return
	 */
	public static Authentication getAuthentication() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext.getAuthentication() != null) {
			return securityContext.getAuthentication();
		}
		return null;
	}

	/**
	 * 解析权限
	 *
	 * @param authentication
	 * @return
	 */
	private static String extractPrincipal(Authentication authentication) {
		if (authentication == null) {
			return null;
		}
		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails.getUsername();
		}
		if (authentication.getPrincipal() instanceof String) {
			return (String) authentication.getPrincipal();
		}
		return null;
	}

	/**
	 * 获取权限集合
	 *
	 * @param authentication
	 * @return
	 */
	private static Stream<String> getAuthorities(Authentication authentication) {
		return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
	}
}
