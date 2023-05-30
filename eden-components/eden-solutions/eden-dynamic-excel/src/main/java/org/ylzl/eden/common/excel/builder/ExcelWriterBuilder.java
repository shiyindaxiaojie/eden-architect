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

import org.ylzl.eden.common.excel.ExcelType;
import org.ylzl.eden.common.excel.ExcelWriter;
import org.ylzl.eden.common.excel.config.ExcelConfig;
import org.ylzl.eden.extension.SPI;

/**
 * Excel 写入器构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI("easy-excel")
public interface ExcelWriterBuilder {

	/**
	 * 设置 Excel 配置
	 *
	 * @param config Excel 配置
	 * @return this
	 */
	ExcelWriterBuilder config(ExcelConfig config);

	/**
	 * 构建 Excel 写入器
	 *
	 * @param excelType 文件类型
	 * @param inMemory  是否在内存操作
	 * @return Excel 写入器实例
	 */
	ExcelWriter build(ExcelType excelType, boolean inMemory);
}
