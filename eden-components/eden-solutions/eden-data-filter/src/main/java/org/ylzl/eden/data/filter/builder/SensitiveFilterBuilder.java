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

package org.ylzl.eden.data.filter.builder;

import org.ylzl.eden.data.filter.SensitiveFilter;
import org.ylzl.eden.data.filter.config.SensitiveConfig;
import org.ylzl.eden.data.filter.sensitive.SensitiveWordLoader;
import org.ylzl.eden.extension.SPI;

/**
 * 敏感词过滤构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI("aho-corasick")
public interface SensitiveFilterBuilder {

	/**
	 * 设置敏感词过滤配置
	 *
	 * @param sensitiveConfig 敏感词过滤配置
	 * @return DataSensitiveFilterBuilder
	 */
	SensitiveFilterBuilder sensitiveConfig(SensitiveConfig sensitiveConfig);

	/**
	 * 设置敏感词处理器
	 *
	 * @param sensitiveWordLoader 敏感词处理器
	 * @return DataSensitiveFilterBuilder
	 */
	SensitiveFilterBuilder sensitiveWordProcessor(SensitiveWordLoader sensitiveWordLoader);

	/**
	 * 构建敏感词过滤器
	 *
	 * @return 敏感词过滤器
	 */
	SensitiveFilter build();
}
