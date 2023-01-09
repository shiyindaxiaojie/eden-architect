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

package org.ylzl.eden.data.filter.sensitive;

import java.util.Collection;

/**
 * 敏感词处理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface SensitiveWordProcessor {

	/**
	 * 加载敏感词库
	 *
	 * @return 敏感词库
	 */
	Collection<String> loadSensitiveWords();

	/**
	 * 敏感词处理
	 *
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 * @param sensitiveWords 发现的敏感词集合
	 * @return 处理后的字段值
	 */
	String handle(String fieldName, String fieldValue, Collection<SensitiveWord> sensitiveWords);

	/**
	 * 清空敏感词库
	 */
	void clearSensitiveWords();

	/**
	 * 重载敏感词库
	 *
	 * @return 已更新的敏感词库
	 */
	default Collection<String> reloadSensitiveWords() {
		clearSensitiveWords();
		return loadSensitiveWords();
	}
}
