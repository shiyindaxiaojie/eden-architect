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

package org.ylzl.eden.spring.framework.logging.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.support.AopUtils;
import org.ylzl.eden.spring.framework.logging.config.AccessLogConfig;
import org.ylzl.eden.spring.framework.logging.util.AccessLogHelper;

import java.time.Duration;
import java.time.Instant;

/**
 * 访问日志拦截器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class AccessLogInterceptor implements MethodInterceptor {

	private final AccessLogConfig config;

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
		if (!AccessLogHelper.shouldLog(config.getSampleRate())) {
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
			long duration = Duration.between(start, Instant.now()).toMillis();
			AccessLogHelper.log(invocation, result, throwable, duration,
				config.isEnabledMdc(), config.getMaxLength(), config.getSlowThreshold());
		}
	}
}
