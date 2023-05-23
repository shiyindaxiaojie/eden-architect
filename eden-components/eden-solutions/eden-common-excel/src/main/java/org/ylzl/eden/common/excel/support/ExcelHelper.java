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

package org.ylzl.eden.common.excel.support;

import org.ylzl.eden.common.excel.ExcelReader;
import org.ylzl.eden.common.excel.ExcelType;
import org.ylzl.eden.common.excel.ExcelWriter;
import org.ylzl.eden.common.excel.builder.ExcelReaderBuilder;
import org.ylzl.eden.common.excel.builder.ExcelWriterBuilder;
import org.ylzl.eden.extension.ExtensionLoader;

/**
 * Excel 帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ExcelHelper {

	/**
	 * 获取 Excel 读取器实例
	 *
	 * @return Excel 读取器实例
	 */
	public static ExcelReader reader() {
		return ExtensionLoader.getExtensionLoader(ExcelReaderBuilder.class)
			.getDefaultExtension()
			.build(1, true);
	}

	/**
	 * 获取 Excel 读取器实例
	 *
	 * @param headRowNumber  标题行数
	 * @param ignoreEmptyRow 忽略空行
	 * @return Excel 读取器实例
	 */
	public static ExcelReader reader(int headRowNumber, boolean ignoreEmptyRow) {
		return ExtensionLoader.getExtensionLoader(ExcelReaderBuilder.class)
			.getDefaultExtension()
			.build(headRowNumber, ignoreEmptyRow);
	}

	/**
	 * 获取 Excel 读取器实例
	 *
	 * @param spi 扩展点
	 * @return Excel 读取器实例
	 */
	public static ExcelReader reader(String spi) {
		return ExtensionLoader.getExtensionLoader(ExcelReaderBuilder.class)
			.getExtension(spi)
			.build(1, true);
	}

	/**
	 * 获取 Excel 读取器实例
	 *
	 * @param spi            扩展点
	 * @param headRowNumber  标题行数
	 * @param ignoreEmptyRow 忽略空行
	 * @return Excel 读取器实例
	 */
	public static ExcelReader reader(String spi, int headRowNumber, boolean ignoreEmptyRow) {
		return ExtensionLoader.getExtensionLoader(ExcelReaderBuilder.class)
			.getExtension(spi)
			.build(headRowNumber, ignoreEmptyRow);
	}

	/**
	 * 获取 Excel 生成器实例
	 *
	 * @return Excel 生成器实例
	 */
	public static ExcelWriter writer() {
		return ExtensionLoader.getExtensionLoader(ExcelWriterBuilder.class)
			.getDefaultExtension()
			.build(ExcelType.XLSX, true);
	}

	/**
	 * 获取 Excel 生成器实例
	 *
	 * @param excelType 文件类型
	 * @param inMemory  是否在内存操作
	 * @return Excel 生成器实例
	 */
	public static ExcelWriter writer(ExcelType excelType, boolean inMemory) {
		return ExtensionLoader.getExtensionLoader(ExcelWriterBuilder.class)
			.getDefaultExtension()
			.build(excelType, inMemory);
	}

	/**
	 * 获取 Excel 生成器实例
	 *
	 * @param spi 扩展点
	 * @return Excel 生成器实例
	 */
	public static ExcelWriter writer(String spi) {
		return ExtensionLoader.getExtensionLoader(ExcelWriterBuilder.class)
			.getExtension(spi)
			.build(ExcelType.XLSX, true);
	}

	/**
	 * 获取 Excel 生成器实例
	 *
	 * @param spi       扩展点
	 * @param excelType 文件类型
	 * @param inMemory  是否在内存操作
	 * @return Excel 生成器实例
	 */
	public static ExcelWriter writer(String spi, ExcelType excelType, boolean inMemory) {
		return ExtensionLoader.getExtensionLoader(ExcelWriterBuilder.class)
			.getExtension(spi)
			.build(excelType, inMemory);
	}
}
