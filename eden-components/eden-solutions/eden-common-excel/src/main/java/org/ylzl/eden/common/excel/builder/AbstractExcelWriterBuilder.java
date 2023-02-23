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

package org.ylzl.eden.common.excel.builder;

import org.ylzl.eden.common.excel.config.ExcelConfig;

/**
 * Excel 写入器构建抽象
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class AbstractExcelWriterBuilder implements ExcelWriterBuilder {

	private ExcelConfig config = new ExcelConfig();

	/**
	 * 设置 Excel 写入器配置
	 *
	 * @param config Excel 写入器配置
	 * @return this
	 */
	@Override
	public ExcelWriterBuilder config(ExcelConfig config) {
		this.config = config;
		return this;
	}

	/**
	 * 获取 Excel 写入器配置
	 *
	 * @return Excel 写入器配置
	 */
	protected ExcelConfig getConfig() {
		return config;
	}
}
