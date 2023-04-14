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

package org.ylzl.eden.spring.framework.aop.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.support.AopUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

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
		// 排除代理类
		if (AopUtils.isAopProxy(invocation.getThis())) {
			return invocation.proceed();
		}

		// 判断是否需要输出日志
		if (!shouldLog()) {
			return invocation.proceed();
		}

		Instant start = Instant.now();
		Object result = null;
		Throwable throwable = null;
		try {
			result = invocation.proceed();
			return result;
		} catch (Throwable t) {
			throwable = t;
			throw t;
		} finally {
			long elapsedMillis = Duration.between(start, Instant.now()).toMillis();
			logInvocation(invocation, result, throwable, elapsedMillis);
		}
	}

	/**
	 * 根据采样率判断是否需要输出日志
	 *
	 * @return
	 */
	private boolean shouldLog() {
		double sampleRate = config.getSampleRate();
		return sampleRate >= 1.0 || Math.random() < sampleRate;
	}

	/**
	 *
	 *
	 * @param invocation
	 * @param result
	 * @param throwable
	 * @param elapsedMillis
	 */
	private void logInvocation(MethodInvocation invocation, Object result, Throwable throwable, long elapsedMillis) {
		StringBuilder sb = new StringBuilder();
		sb.append(Objects.requireNonNull(invocation.getThis()).getClass().getName());
		sb.append(".");
		sb.append(invocation.getMethod().getName());
		sb.append("(");

		Object[] args = invocation.getArguments();
		for (int i = 0; i < args.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(args[i] == null ? "null" : args[i].toString());
		}

		sb.append(")");

		if (throwable != null) {
			sb.append(" threw exception: ");
			sb.append(throwable);
		} else {
			sb.append(" returned: ");
			sb.append(result);
		}

		sb.append(" (");
		sb.append(elapsedMillis);
		sb.append("ms)");

		log.info(sb.toString());
	}
}
