/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.security.jwt.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.spring.framework.core.constant.SpringFrameworkConstants;
import org.ylzl.eden.spring.framework.web.errors.UnauthorizedException;
import org.ylzl.eden.spring.security.core.login.Login;

/**
 * JWT 身份验证服务
 *
 * @author gyl
 * @since 2.4.x
 */
@Slf4j
public class JwtTokenService {

	private static final String EXP_AUTHENTICATE_BAD_CREDENTIALS =
		"JWT authenticated failed due to bad credentials：{}";

	private static final String EXP_AUTHENTICATE_EXCEPTION =
		"JWT authenticated failed, caught exception: {}";

	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider jwtTokenProvider;

	public JwtTokenService(
		AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	/**
	 * 认证
	 *
	 * @param login 登录数据传输对象
	 * @return JWT 访问令牌
	 */
	public JwtToken authenticate(Login login) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
			Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			StringBuilder claim = new StringBuilder();
			if (authentication.getAuthorities() != null) {
				for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
					claim.append(grantedAuthority.getAuthority()).append(StringConstants.COMMA);
				}
				claim.deleteCharAt(claim.length() - 1);
			}
			boolean rememberMe = (login.getRememberMe() == null) ? false : login.getRememberMe();
			String token =
				jwtTokenProvider.build(
					login.getUsername(), SpringFrameworkConstants.SYSTEM, claim.toString(), rememberMe);
			return JwtToken.builder().value(token).build();
		} catch (BadCredentialsException ex) {
			log.error(EXP_AUTHENTICATE_BAD_CREDENTIALS, ex.getMessage(), ex);
			throw new UnauthorizedException(ex.getMessage());
		} catch (Exception ex) {
			log.error(EXP_AUTHENTICATE_EXCEPTION, ex.getMessage(), ex);
			throw ex;
		}
	}
}
