package org.ylzl.eden.idempotent.web.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ylzl.eden.idempotent.core.Idempotent;
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
public class IdempotentTokenInterceptor implements HandlerInterceptor {

	private final TokenIdempotentStrategy tokenIdempotentStrategy;

	@Setter
	private String tokenName = "idempotent";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
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
