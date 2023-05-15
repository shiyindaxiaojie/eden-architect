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

import org.ylzl.eden.data.filter.DuplicateFilter;
import org.ylzl.eden.data.filter.config.DuplicateConfig;
import org.ylzl.eden.extension.SPI;

/**
 * 数据去重过滤构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI("bloom-filter")
public interface DuplicateFilterBuilder {

	/**
	 * 设置数据去重过滤配置
	 *
	 * @param config 数据去重过滤配置
	 * @return DataDuplicateFilterBuilder
	 */
	DuplicateFilterBuilder config(DuplicateConfig config);

	/**
	 * 构建数据去重过滤器
	 *
	 * @return 数据去重过滤器
	 */
	DuplicateFilter build();
}
