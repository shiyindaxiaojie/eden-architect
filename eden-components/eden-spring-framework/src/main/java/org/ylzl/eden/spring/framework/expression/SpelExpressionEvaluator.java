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

package org.ylzl.eden.spring.framework.expression;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * SpEL 表达式解析器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SpelExpressionEvaluator {

	private static final ExpressionParser PARSER = new SpelExpressionParser();

	private static final ParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

	public static ExpressionParser getExpressionParser() {
		return PARSER;
	}

	public static ParameterNameDiscoverer getParameterNameDiscoverer() {
		return DISCOVERER;
	}

	/**
	 * 解析 SpEL 表达式
	 *
	 * @param expressionString SpEL 表达式
	 * @param method           方法
	 * @param arguments        参数
	 * @return 解析后的内容
	 */
	public static String parseExpression(String expressionString, Method method, Object[] arguments) {
		String[] params = DISCOVERER.getParameterNames(method);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				SpelEvaluationContext.setVariable(params[i], arguments[i]);
			}
		}

		Expression expression = PARSER.parseExpression(expressionString);
		return expression.getValue(SpelEvaluationContext.getContext(), String.class);
	}
}
