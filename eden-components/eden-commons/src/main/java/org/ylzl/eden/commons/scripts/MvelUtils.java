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

import java.io.Serializable;
import java.util.Map;

/**
 * MVEL 脚本语言工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class MvelUtils {

	public static <T> T executeExpression(String expression, Class<T> clazz) {
		return executeExpression(expression, clazz, null);
	}

	public static <T> T executeExpression(String expression, Class<T> clazz, Map<String, Object> vars) {
		Serializable compileExpression = MVEL.compileExpression(expression);
		return MVEL.executeExpression(compileExpression, vars, clazz);
	}
}
