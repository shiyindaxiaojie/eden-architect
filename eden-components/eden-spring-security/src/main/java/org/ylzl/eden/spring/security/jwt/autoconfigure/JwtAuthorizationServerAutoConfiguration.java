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

package org.ylzl.eden.spring.security.jwt.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.ylzl.eden.spring.security.core.autoconfigure.DefaultWebSecuirtyConfiguration;
import org.ylzl.eden.spring.security.core.constant.SpringSecurityConstants;
import org.ylzl.eden.spring.security.jwt.endpoint.JwtTokenEndpoint;
import org.ylzl.eden.spring.security.jwt.env.JwtProperties;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenProvider;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenService;

/**
 * JWT 授权服务端自动装配
 *
 * @author gyl
 * @since 2.4.x
 */
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnExpression(JwtAuthorizationServerAutoConfiguration.EXP_JWT_AUTHORIZATION_SERVER)
@Import({DefaultWebSecuirtyConfiguration.class, JwtWebSecurityConfiguration.class})
@Slf4j
@Configuration
public class JwtAuthorizationServerAutoConfiguration {

	public static final String EXP_JWT_AUTHORIZATION_SERVER =
		"${" + SpringSecurityConstants.PROP_PREFIX + ".jwt.authorization.server.enabled:false}";

	private final JwtProperties jwtProperties;

	public JwtAuthorizationServerAutoConfiguration(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	/**
	 * Inject JWT 令牌提供器
	 *
	 * @return 单例对象
	 */
	@ConditionalOnMissingBean
	@Bean
	public JwtTokenProvider jwtTokenProvider() {
		return new JwtTokenProvider(jwtProperties);
	}

	/**
	 * Inject JWT 令牌服务
	 *
	 * @param authenticationManager 认证管理器
	 * @param jwtTokenProvider      JWT 令牌提供器
	 * @return 单例对象
	 */
	@ConditionalOnMissingBean
	@Bean
	public JwtTokenService jwtTokenService(
		AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		return new JwtTokenService(authenticationManager, jwtTokenProvider);
	}

	/**
	 * Inject JWT 令牌端点
	 *
	 * @param jwtTokenService JWT 令牌服务
	 * @return 单例对象
	 */
	@ConditionalOnMissingBean
	@Bean
	public JwtTokenEndpoint jwtTokenEndpoint(JwtTokenService jwtTokenService) {
		return new JwtTokenEndpoint(jwtTokenService, jwtProperties);
	}
}
