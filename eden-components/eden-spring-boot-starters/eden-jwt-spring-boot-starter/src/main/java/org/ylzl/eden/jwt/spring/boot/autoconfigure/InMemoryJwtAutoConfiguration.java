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

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.jwt.spring.boot.env.JwtProperties;
import org.ylzl.eden.spring.security.jwt.model.AccessToken;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenStore;


/**
 * Jwt 基于内存自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnExpression(JwtAutoConfiguration.SECURITY_JWT_ENABLED)
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class InMemoryJwtAutoConfiguration {

	private static final String AUTOWIRED_IN_MEMORY_JWT_TOKEN_STORE = "Autowired InMemoryJwtTokenStore";

	@ConditionalOnMissingBean
	@Bean
	public JwtTokenStore tokenStore() {
		log.debug(AUTOWIRED_IN_MEMORY_JWT_TOKEN_STORE);
		return new JwtTokenStore() {

			@Override
			public boolean validateAccessToken(AccessToken token) {
				return true;
			}

			@Override
			public void storeAccessToken(AccessToken token) {

			}

			@Override
			public void removeAccessToken(AccessToken token) {

			}
		};
	}
}
