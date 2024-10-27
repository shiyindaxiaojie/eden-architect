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

package org.ylzl.eden.commons.crypto;

import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * 解密异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class DecryptException extends RuntimeException {

	public DecryptException(String message, Object... params) {
		super(MessageFormatUtils.format(message, params));
	}

	public DecryptException(String message, Throwable cause, Object... params) {
		super(MessageFormatUtils.format(message, params), cause);
	}

	protected DecryptException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace, Object... params) {
		super(MessageFormatUtils.format(message, params), cause, enableSuppression, writableStackTrace);
	}
}
