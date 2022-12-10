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

package org.ylzl.eden.spring.framework.error;

import org.jetbrains.annotations.PropertyKey;
import org.ylzl.eden.commons.lang.format.MessageFormatter;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 错误码加载器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ErrorCodeLoader {

	public static final String BUNDLE_NAME = "META-INF.error.message";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME,
		Locale.SIMPLIFIED_CHINESE);

	public static String getErrMessage(@PropertyKey(resourceBundle = BUNDLE_NAME) String errCode,
									   Object... params) {
		if (RESOURCE_BUNDLE.containsKey(errCode)) {
			String value = RESOURCE_BUNDLE.getString(errCode);
			return MessageFormatter.arrayFormat(value, params).getMessage();
		}
		return MessageFormatter.arrayFormat(errCode, params).getMessage();
	}
}
