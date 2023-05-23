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

package org.ylzl.eden.commons.lang;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.format.MessageFormatter;

/**
 * 消息格式化工具
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class MessageFormatUtils {

	/**
	 * 格式化消息内容
	 *
	 * @param message      消息
	 * @param placeholders 占位符
	 * @return 格式化内容
	 */
	public static String format(String message, Object... placeholders) {
		return MessageFormatter.arrayFormat(message, placeholders).getMessage();
	}
}
