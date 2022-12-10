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

package org.ylzl.eden.idempotent.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.idempotent.Idempotent;
import org.ylzl.eden.idempotent.strategy.IdempotentStrategy;
import org.ylzl.eden.idempotent.strategy.TtlIdempotentStrategy;
import org.ylzl.eden.spring.framework.aop.util.AspectJAopUtils;
import org.ylzl.eden.spring.framework.web.util.RequestUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 幂等性切面
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Aspect
public class IdempotentTtlAspect {

	private final TtlIdempotentStrategy strategy;

	@Pointcut("@within(org.ylzl.eden.idempotent.Idempotent) && execution(public * *(..))")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		if (method.isAnnotationPresent(Idempotent.class)) {
			Idempotent idempotent = method.getAnnotation(Idempotent.class);
			if (IdempotentStrategy.TTL == idempotent.strategy()) {
				String key = resolveKey(idempotent.key(), joinPoint);
				strategy.checkFirstRequest(key, Strings.EMPTY, idempotent.ttl(), idempotent.timeUnit());
			}
		}
		return joinPoint.proceed();
	}

	private String resolveKey(String key, ProceedingJoinPoint joinPoint) {
		if (StringUtils.isNotBlank(key)) {
			return AspectJAopUtils.parseSpelExpression(key, joinPoint);
		}
		String url = RequestUtils.getRequestURL();
		String argString = Arrays.asList(joinPoint.getArgs()).toString();
		return url + argString;
	}
}
