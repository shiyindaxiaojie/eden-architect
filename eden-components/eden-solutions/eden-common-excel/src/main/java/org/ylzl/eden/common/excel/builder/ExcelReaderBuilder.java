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

import org.ylzl.eden.common.excel.ExcelReader;
import org.ylzl.eden.common.excel.config.ExcelConfig;
import org.ylzl.eden.extension.SPI;

/**
 * Excel 读取器构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI("easy-excel")
public interface ExcelReaderBuilder {

	/**
	 * 设置 Excel 配置
	 *
	 * @param config Excel 配置
	 * @return this
	 */
	ExcelReaderBuilder config(ExcelConfig config);

	/**
	 * 构建 Excel 读取器
	 *
	 * @param headRowNumber 标题行数
	 * @param ignoreEmptyRow 忽略空行
	 * @return Excel 读取器实例
	 */
	ExcelReader build(int headRowNumber, boolean ignoreEmptyRow);
}
