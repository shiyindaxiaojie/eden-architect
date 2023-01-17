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

package org.ylzl.eden.spring.framework.aop.util;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;
import org.ylzl.eden.spring.framework.expression.SpelExpressionEvaluator;

import java.lang.reflect.Method;

/**
 * AspectJ 工具
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class AspectJAopUtils extends org.springframework.aop.aspectj.AspectJAopUtils {

	private static final String SPEL_EXPRESSION_IS_INVALID = "Spel expression is invalid: {}";

	/**
	 * 从切点获取 Method
	 *
	 * @param joinPoint
	 * @return
	 */
	public static Method getMethod(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		if (method.getDeclaringClass().isInterface()) {
			try {
				method = joinPoint.getTarget().getClass().getDeclaredMethod(joinPoint.getSignature().getName(),
					method.getParameterTypes());
			} catch (SecurityException | NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
		return method;
	}

	/**
	 * 解析 Spel 表达式
	 *
	 * @param expressionString
	 * @param joinPoint
	 * @return
	 */
	public static String parseExpression(String expressionString, JoinPoint joinPoint) {
		Object[] arguments = joinPoint.getArgs();
		Method method = getMethod(joinPoint);
		String resolveValue = SpelExpressionEvaluator.parseExpression(expressionString, method, arguments);
		AssertUtils.notNull(resolveValue, MessageFormatUtils.format(SPEL_EXPRESSION_IS_INVALID, expressionString));
		return resolveValue;
	}
}
