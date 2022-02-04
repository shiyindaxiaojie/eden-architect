/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.framework.core.util;

import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.Collection;

/**
 * 断言工具集
 *
 * @author gyl
 * @since 2.0.0
 */
public class SpringAssert extends Assert {

	public static void state(boolean expression, String pattern, Object... args) {
		state(expression, MessageFormat.format(pattern, args));
	}

	public static void notNull(Object object, String pattern, Object... args) {
		notNull(object, MessageFormat.format(pattern, args));
	}

	public static void isNull(Object object, String pattern, Object... args) {
		isNull(object, MessageFormat.format(pattern, args));
	}

	public static void hasLength(String text, String pattern, Object... args) {
		hasLength(text, MessageFormat.format(pattern, args));
	}

	public static void hasText(String text, String pattern, Object... args) {
		hasText(text, MessageFormat.format(pattern, args));
	}

	public static void notEmpty(Object[] array, String pattern, Object... args) {
		notEmpty(array, MessageFormat.format(pattern, args));
	}

	public static void notEmpty(Collection<?> collection, String pattern, Object... args) {
		notEmpty(collection, MessageFormat.format(pattern, args));
	}

	public static void noNullElements(Object[] array, String pattern, Object... args) {
		noNullElements(array, MessageFormat.format(pattern, args));
	}
}
