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

package org.ylzl.eden.spring.integration.cat.aop;

import com.dianping.cat.Cat;
import com.site.lookup.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.AopProxyUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.integration.cat.core.CatLogMetricForCount;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Cat.logMetricForCount 方法返回拦截
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class CatLogMetricForCountAdvice implements MethodInterceptor {

	private final CatLogMetricForCount catLogMetricForCount;

	@Override
	public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		Object target = invocation.getThis();
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(Objects.requireNonNull(target));
		String name = StringUtils.isNotEmpty(catLogMetricForCount.name())?
			catLogMetricForCount.name() : targetClass.getSimpleName() + Strings.DOT + method.getName();
		try {
			return invocation.proceed();
		} finally {
			Cat.logMetricForCount(name, catLogMetricForCount.count());
		}
	}
}
