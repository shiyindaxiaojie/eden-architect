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

package org.ylzl.eden.spring.framework.error;

import lombok.NonNull;
import org.jetbrains.annotations.PropertyKey;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 第三方服务
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see ClientAssert
 * @see ServerAssert
 * @since 2.4.13
 */
public class ThirdServiceAssert {

	public static void doesNotContain(@NonNull String textToSearch, String substring,
									  @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.doesNotContain(textToSearch, substring, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void hasLength(@NonNull String expression,
								 @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.hasLength(expression, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void hasText(String text,
							   @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.hasText(text, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void isInstanceOf(Class<?> type, @NonNull Object obj,
									@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.isInstanceOf(type, obj, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void isNull(Object object,
							  @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.isNull(object, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void notNull(Object object,
							   @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.notNull(object, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void isTrue(boolean expression,
							  @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.isTrue(expression, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void noNullElements(@NonNull Collection<?> collection,
									  @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.noNullElements(collection, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void notEmpty(@NonNull Object[] array,
								@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.notEmpty(array, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void notEmpty(@NonNull Collection<?> collection,
								@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.notEmpty(collection, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void notEmpty(@NonNull Map<?, ?> map,
								@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.notEmpty(map, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void isAssignable(Class<?> superType, @NonNull Class<?> subType,
									@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.isAssignable(superType, subType, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}

	public static void state(boolean expression,
							 @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		try {
			AssertUtils.state(expression, errCode, placeholders);
		} catch (IllegalArgumentException e) {
			throw new ThirdServiceException(errCode, e.getMessage());
		}
	}
}

