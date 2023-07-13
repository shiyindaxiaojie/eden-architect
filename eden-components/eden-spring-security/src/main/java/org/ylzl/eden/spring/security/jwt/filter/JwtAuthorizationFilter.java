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

package org.ylzl.eden.spring.security.jwt.filter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.PathMatcher;
import org.ylzl.eden.spring.framework.web.util.ServletUtils;
import org.ylzl.eden.spring.security.common.token.AccessToken;
import org.ylzl.eden.spring.security.jwt.constant.JwtConstants;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * JWT 过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private static final String TOKEN_IS_REQUIRED = "Token is required";

	private final JwtTokenProvider jwtTokenProvider;

	private final PathMatcher pathMatcher;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
								  JwtTokenProvider jwtTokenProvider, PathMatcher pathMatcher) {
		super(authenticationManager);
		this.jwtTokenProvider = jwtTokenProvider;
		this.pathMatcher = pathMatcher;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain chain) throws IOException, ServletException {
		if (!isAnonymousUrls(request)) {
			AccessToken accessToken = resolveToken(request);
			if (accessToken == null) {
				ServletUtils.wrap(response, HttpServletResponse.SC_UNAUTHORIZED, "USER-AUTH-400", TOKEN_IS_REQUIRED);
				return;
			}
			try {
				this.jwtTokenProvider.validateToken(accessToken);
			} catch (Exception e) {
				ServletUtils.wrap(response, HttpServletResponse.SC_UNAUTHORIZED, "USER-AUTH-400", e.getMessage());
				return;
			}
			Authentication authentication = this.jwtTokenProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}

	private AccessToken resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(jwtTokenProvider.getJwtConfig().getHeader());
		if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(JwtConstants.BEARER_PREFIX)) {
			return AccessToken.builder().value(bearerToken.substring(JwtConstants.BEARER_PREFIX.length())).build();
		}
		return null;
	}

	private boolean isAnonymousUrls(HttpServletRequest request) {
		List<String> anonymousUrls = jwtTokenProvider.getJwtConfig().getAnonymousUrls();
		if (CollectionUtils.isEmpty(anonymousUrls)) {
			return false;
		}
		String requestURI = request.getRequestURI();
		return anonymousUrls.stream().anyMatch(url -> pathMatcher.match(url, requestURI));
	}
}
