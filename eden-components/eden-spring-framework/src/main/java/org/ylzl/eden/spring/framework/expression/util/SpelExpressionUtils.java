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

package org.ylzl.eden.spring.framework.expression.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * Spel 表达式解析工具
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class SpelExpressionUtils {

	private static final SpelExpressionParser PARSER = new SpelExpressionParser();

	private static final LocalVariableTableParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * 解析 Spel 表达式
	 *
	 * @param expressionString
	 * @param method
	 * @param arguments
	 * @return
	 */
	public static String parse(String expressionString, Method method, Object[] arguments) {
		String[] params = DISCOVERER.getParameterNames(method);
		StandardEvaluationContext context = new StandardEvaluationContext();

		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				context.setVariable(params[i], arguments[i]);
			}
		}

		Expression expression = PARSER.parseExpression(expressionString);
		return expression.getValue(context, String.class);
	}
}
