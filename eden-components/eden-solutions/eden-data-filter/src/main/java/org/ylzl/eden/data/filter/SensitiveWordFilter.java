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

package org.ylzl.eden.data.filter;

import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.data.filter.sensitive.SensitiveWord;

import java.util.Collection;

/**
 * 数据敏感词过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface SensitiveWordFilter {

	/**
	 * 解析文本
	 *
	 * @param text 原始内容
	 * @return 已解析的敏感词
	 */
	Collection<SensitiveWord> parseText(String text);

	/**
	 * 替换敏感词
	 *
	 * @param text 原始内容
	 * @param replacement 替换内容
	 * @return 过滤后的内容
	 */
	String replaceSensitiveWords(String text, String replacement);

	/**
	 * 替换敏感词
	 *
	 * @param text 原始内容
	 * @return 过滤后的内容
	 */
	default String replaceSensitiveWords(String text) {
		return replaceSensitiveWords(text, Sensitive.DEFAULT_REPLACEMENT);
	}

	/**
	 * 删除敏感词
	 *
	 * @param text 原始内容
	 * @return 过滤后的内容
	 */
	default String deleteSensitiveWords(String text) {
		return replaceSensitiveWords(text, Strings.EMPTY);
	}
}
