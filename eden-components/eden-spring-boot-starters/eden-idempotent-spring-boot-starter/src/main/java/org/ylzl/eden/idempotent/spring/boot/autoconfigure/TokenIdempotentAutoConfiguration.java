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

package org.ylzl.eden.idempotent.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.idempotent.spring.boot.env.IdempotentTokenProperties;
import org.ylzl.eden.idempotent.strategy.TokenIdempotentStrategy;
import org.ylzl.eden.idempotent.web.controller.IdempotentTokenController;
import org.ylzl.eden.idempotent.web.interceptor.IdempotentTokenInterceptor;

/**
 * 幂等请求自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(prefix = IdempotentTokenProperties.PREFIX, matchIfMissing = true)
@EnableConfigurationProperties(IdempotentTokenProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class TokenIdempotentAutoConfiguration {

	private static final String AUTOWIRED_IDEMPOTENT_TOKEN_CONTROLLER = "Autowired IdempotentTokenController";

	private static final String AUTOWIRED_IDEMPOTENT_TOKEN_INTERCEPTOR = "Autowired IdempotentTokenInterceptor";

	private final IdempotentTokenProperties properties;

	@Bean
	public IdempotentTokenController idempotentTokenController(TokenIdempotentStrategy strategy) {
		log.debug(AUTOWIRED_IDEMPOTENT_TOKEN_CONTROLLER);
		return new IdempotentTokenController(strategy);
	}

	@Bean
	public IdempotentTokenInterceptor idempotentTokenInterceptor(TokenIdempotentStrategy strategy) {
		log.debug(AUTOWIRED_IDEMPOTENT_TOKEN_INTERCEPTOR);
		IdempotentTokenInterceptor interceptor = new IdempotentTokenInterceptor(strategy);
		if (StringUtils.isNotBlank(properties.getTokenName())) {
			interceptor.setTokenName(properties.getTokenName());
		}
		return interceptor;
	}
}
