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

package org.ylzl.eden.jwt.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.ylzl.eden.jwt.spring.boot.env.JwtProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.eden.spring.security.jwt.config.JwtConfig;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenProvider;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenService;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenStore;

/**
 * JWT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = JwtProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE
)
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class JwtAutoConfiguration {

	private final JwtProperties jwtProperties;

	@ConditionalOnMissingBean
	@Bean
	public JwtTokenProvider jwtTokenProvider(JwtTokenStore jwtTokenStore) {
		JwtConfig jwtConfig = JwtConfig.builder()
			.header(jwtProperties.getHeader())
			.secret(jwtProperties.getSecret())
			.base64Secret(jwtProperties.getBase64Secret())
			.tokenValidityInSeconds(jwtProperties.getTokenValidityInSeconds())
			.tokenValidityInSecondsForRememberMe(jwtProperties.getTokenValidityInSecondsForRememberMe())
			.anonymousUrls(jwtProperties.getAnonymousUrls())
			.authenticatedUrls(jwtProperties.getAuthenticatedUrls())
			.permitAllUrls(jwtProperties.getPermitAllUrls())
			.build();
		return new JwtTokenProvider(jwtConfig, jwtTokenStore);
	}

	@ConditionalOnMissingBean
	@Bean
	public JwtTokenService jwtTokenService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		return new JwtTokenService(authenticationManager, jwtTokenProvider);
	}
}
