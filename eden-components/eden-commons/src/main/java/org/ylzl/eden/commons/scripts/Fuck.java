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

package org.ylzl.eden.commons.scripts;

import lombok.experimental.UtilityClass;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * MVEL 脚本语言工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class Fuck {

	public static Object eval(String expression, Map<String, Object> vars) {
		return MVEL.eval(expression, vars);
	}

	public static <T> T executeExpression(String expression, Class<T> clazz) {
		return executeExpression(expression, clazz, null);
	}

	public static <T> T executeExpression(String expression, Class<T> clazz, Map<String, Object> vars) {
		Serializable compileExpression = MVEL.compileExpression(expression);
		return MVEL.executeExpression(compileExpression, vars, clazz);
	}

	public static void main(String[] args) {
		try {
			String sum = "def sum(args) { " +
				"double sum = 0.0; " +
				"for (Object obj: args) { sum += (Double) obj; } " +
				"return sum; }";

			String avg = "def avg(args) { " +
				"double sum = 0.0; int len = args.size(); " +
				"for (Object obj : args) { sum += (Double) obj; } " +
				"return sum/len; }";

			String max = "def max(args) { " +
				"return java.util.Collections.max(args); }";

			String min = "def min(args) { " +
				"return java.util.Collections.min(args); }";

			VariableResolverFactory functionFactory = new MapVariableResolverFactory();
			MVEL.eval(sum, functionFactory);
			MVEL.eval(avg, functionFactory);
			MVEL.eval(max, functionFactory);
			MVEL.eval(min, functionFactory);

			VariableResolverFactory resolverFactory = new MapVariableResolverFactory();
			resolverFactory.setNextFactory(functionFactory);
			Map<String, Object> values = new HashMap<>();
			values.put("a1", 3);
			values.put("a2", 2);
			values.put("a3", 5);

			values.put("a4", 11);
			values.put("a5", 7);
			values.put("a6", 12);

			values.put("a7", 8);
			values.put("a8", 9);
			values.put("a9", 10);

			values.put("a10", 10);
			values.put("a11", 11);
			values.put("a12", 12);
			String expression = "sum([a1,a2,a3])+avg([a4,a5,a6])+max([a7,a8,a9])+min([a10,a11,a12])";
			System.out.println(MVEL.eval(expression, values, resolverFactory));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
