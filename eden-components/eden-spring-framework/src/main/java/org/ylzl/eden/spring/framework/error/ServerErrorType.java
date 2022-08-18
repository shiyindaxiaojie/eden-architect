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
import org.ylzl.eden.spring.framework.error.util.AssertEnhancer;

import java.util.Collection;
import java.util.Map;

/**
 * 服务端错误
 *
 * <ul>参考《阿里巴巴Java开发手册》错误码
 * <li>A____：表示错误来自于用户</li>
 * <li>B____：表示错误来自于当前系统</li>
 * <li>C____：表示错误来自于第三方服务</li>
 * </ul>
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see ClientErrorType
 * @see ThirdServiceErrorType
 * @since 2.4.13
 */
public class ServerErrorType {

	public static void doesNotContain(@NonNull String textToSearch, String substring,
									  @PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.doesNotContain(textToSearch, substring, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void hasLength(@NonNull String expression,
								 @PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.hasLength(expression, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void hasText(String text,
							   @PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.hasText(text, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void isInstanceOf(Class<?> type, @NonNull Object obj,
									@PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.isInstanceOf(type, obj, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void isNull(Object object,
							  @PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.isNull(object, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void notNull(Object object,
							   @PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.notNull(object, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void isTrue(boolean expression,
							  @PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.isTrue(expression, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void noNullElements(@NonNull Collection<?> collection,
									  @PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.noNullElements(collection, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void notEmpty(@NonNull Object[] array,
								@PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.notEmpty(array, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void notEmpty(@NonNull Collection<?> collection,
								@PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.notEmpty(collection, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void notEmpty(@NonNull Map<?, ?> map,
								@PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.notEmpty(map, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void isAssignable(Class<?> superType, @NonNull Class<?> subType,
									@PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.isAssignable(superType, subType, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}

	public static void state(boolean expression,
							 @PropertyKey(resourceBundle = ErrorConfig.BASE_NAME) String errCode, Object... params) {
		try {
			AssertEnhancer.state(expression, ErrorConfig.getErrMessage(errCode, params));
		} catch (IllegalArgumentException e) {
			throw new ServerException(errCode, e.getMessage());
		}
	}
}
