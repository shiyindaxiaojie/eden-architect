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

package org.ylzl.eden.idempotent.web.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ylzl.eden.idempotent.Idempotent;
import org.ylzl.eden.idempotent.strategy.IdempotentStrategy;
import org.ylzl.eden.idempotent.strategy.TokenIdempotentStrategy;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 幂等令牌拦截器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class TokenIdempotentInterceptor implements HandlerInterceptor {

	private final TokenIdempotentStrategy tokenIdempotentStrategy;

	@Setter
	private String tokenName = "idempotent";

	@Override
	public boolean preHandle(@NotNull HttpServletRequest request,
							 @NotNull HttpServletResponse response, @NotNull Object handler) {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Idempotent idempotent = handlerMethod.getMethod().getAnnotation(Idempotent.class);
		if (idempotent != null && IdempotentStrategy.TOKEN == idempotent.strategy()) {
			String token = request.getHeader(tokenName);
			AssertUtils.notNull(token, "REQ-UNIQUE-409");
			tokenIdempotentStrategy.validateToken(token);
		}
		return true;
	}
}
