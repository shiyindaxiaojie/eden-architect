/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.security;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Spring Security 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class SpringSecurityUtils {

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
