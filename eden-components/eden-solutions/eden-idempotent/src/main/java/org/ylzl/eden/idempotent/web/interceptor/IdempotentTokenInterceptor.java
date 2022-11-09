package org.ylzl.eden.idempotent.web.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ylzl.eden.idempotent.core.Idempotent;
import org.ylzl.eden.idempotent.token.IdempotentTokenProvider;

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

	private final IdempotentTokenProvider idempotentTokenProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Idempotent idempotent = handlerMethod.getMethod().getAnnotation(Idempotent.class);
		if (idempotent != null) {
			idempotentTokenProvider.validate(null);
		}
		return true;
	}
}
