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
import com.dianping.cat.message.Transaction;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.ylzl.eden.commons.lang.Strings;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * CatTransaction 方法拦截
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CatTransactionMethodInterceptor implements MethodInterceptor {

	public static final String SERVICE = "RPC";

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		Object target = invocation.getThis();
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(Objects.requireNonNull(target));
		String name = targetClass.getSimpleName() + Strings.DOT + method.getName();
		Transaction transaction = Cat.newTransaction(SERVICE, name);
		try {
			return invocation.proceed();
		} finally {
			if (transaction != null) {
				transaction.complete();
			}
		}
	}
}
