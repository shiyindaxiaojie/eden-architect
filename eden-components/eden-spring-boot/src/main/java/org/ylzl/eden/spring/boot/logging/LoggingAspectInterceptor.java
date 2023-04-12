package org.ylzl.eden.spring.boot.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.support.AopUtils;

/**
 * 日志拦截器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class LoggingAspectInterceptor implements MethodInterceptor {

	private final LoggingAspectConfig config;

	/**
	 * 方法调用拦截处理
	 *
	 * @param invocation 方法调用元信息
	 * @return 返回值
	 * @throws Throwable 异常
	 */
	@Override
	public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
		if (AopUtils.isAopProxy(invocation.getThis())) {
			return invocation.proceed();
		}

		StringBuilder logBuilder = new StringBuilder();

		// 判断是否需要输出日志
		if (!shouldLog(config.getSampleRate())) {
			return invocation.proceed();
		}
	}

	/**
	 * 根据采样率判断是否需要输出日志
	 */
	private boolean shouldLog(double sampleRate) {
		return sampleRate >= 1.0 || Math.random() < sampleRate;
	}
}
