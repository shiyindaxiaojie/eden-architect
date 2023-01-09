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

package org.ylzl.eden.data.filter.support;

import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.data.filter.DataSensitiveFilter;
import org.ylzl.eden.data.filter.Sensitive;
import org.ylzl.eden.data.filter.builder.DataSensitiveFilterBuilder;
import org.ylzl.eden.data.filter.sensitive.SensitiveWord;
import org.ylzl.eden.data.filter.sensitive.SensitiveWordLoader;
import org.ylzl.eden.extension.ExtensionLoader;

import java.util.Collection;

/**
 * 敏感词过滤器帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class DataSensitiveFilterHelper {

	/**
	 * 获取敏感词过滤器实例
	 *
	 * @param sensitiveWordLoader 敏感词处理器
	 * @return 敏感词过滤器实例
	 */
	public static DataSensitiveFilter dataSensitiveFilter(SensitiveWordLoader sensitiveWordLoader) {
		return ExtensionLoader.getExtensionLoader(DataSensitiveFilterBuilder.class).getDefaultExtension()
			.sensitiveWordProcessor(sensitiveWordLoader).build();
	}

	/**
	 * 获取敏感词过滤器实例
	 *
	 * @param spi 扩展点
	 * @param sensitiveWordLoader 敏感词处理器
	 * @return 敏感词过滤器实例
	 */
	public static DataSensitiveFilter dataSensitiveFilter(String spi, SensitiveWordLoader sensitiveWordLoader) {
		return ExtensionLoader.getExtensionLoader(DataSensitiveFilterBuilder.class).getExtension(spi)
			.sensitiveWordProcessor(sensitiveWordLoader).build();
	}

	/**
	 * 执行过滤
	 *
	 * @param text 文本内容
	 * @param clazz 目标类型
	 * @param dataSensitiveFilter 敏感词过滤器
	 * @return 过滤后的内容
	 */
	public static String doFilter(String text, Class<?> clazz, DataSensitiveFilter dataSensitiveFilter) {
		Sensitive sensitive = clazz.getAnnotation(Sensitive.class);
		String replacement = null;
		switch (sensitive.strategy()) {
			case NONE:
				return text;
			case DELETE:
				replacement = Strings.EMPTY;
				break;
			case REPLACE:
				replacement = sensitive.replacement();
				break;
		}
		String newText = text;
		Collection<SensitiveWord> sensitiveWords = dataSensitiveFilter.parseText(text);
		for (SensitiveWord sensitiveWord : sensitiveWords) {
			newText = text.replaceAll(sensitiveWord.getKeyword(), replacement);
		}
		return newText;
	}
}
