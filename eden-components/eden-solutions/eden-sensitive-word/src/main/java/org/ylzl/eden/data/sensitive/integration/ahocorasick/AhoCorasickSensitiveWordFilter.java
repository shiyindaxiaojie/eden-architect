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

package org.ylzl.eden.data.sensitive.integration.ahocorasick;

import lombok.RequiredArgsConstructor;
import org.ahocorasick.trie.Trie;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.data.sensitive.SensitiveWordFilter;
import org.ylzl.eden.data.sensitive.SensitiveWord;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * AhoCorasick 敏感词过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class AhoCorasickSensitiveWordFilter implements SensitiveWordFilter {

	private final Trie trie;

	/**
	 * 解析文本
	 *
	 * @param text 原始内容
	 * @return 过滤后的内容
	 */
	@Override
	public Collection<SensitiveWord> parseText(String text) {
		return trie.parseText(text).stream()
			.map(emit -> SensitiveWord.builder()
				.keyword(emit.getKeyword())
				.start(emit.getStart())
				.end(emit.getEnd())
				.build())
			.collect(Collectors.toList());
	}

	/**
	 * 替换敏感词
	 *
	 * @param text        原始内容
	 * @param replacement 替换内容
	 * @return 过滤后的内容
	 */
	@Override
	public String replaceSensitiveWords(String text, String replacement) {
		Collection<SensitiveWord> sensitiveWords = this.parseText(text);
		String regex = StringUtils.join(sensitiveWords.stream().map(SensitiveWord::getKeyword).collect(Collectors.toList()), "|");
		return Pattern.compile(regex).matcher(text).replaceAll(replacement);
	}
}
