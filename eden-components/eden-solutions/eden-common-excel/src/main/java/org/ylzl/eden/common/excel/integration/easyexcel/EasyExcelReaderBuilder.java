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

package org.ylzl.eden.common.excel.integration.easyexcel;

import com.alibaba.excel.EasyExcel;
import org.ylzl.eden.common.excel.ExcelReader;
import org.ylzl.eden.common.excel.builder.AbstractExcelReaderBuilder;
import org.ylzl.eden.common.excel.builder.ExcelReaderBuilder;
import org.ylzl.eden.common.excel.integration.easyexcel.converter.LocalDateConverter;
import org.ylzl.eden.common.excel.integration.easyexcel.converter.LocalDateTimeConverter;

/**
 * EasyExcel 读取 Excel
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class EasyExcelReaderBuilder extends AbstractExcelReaderBuilder implements ExcelReaderBuilder {

	/**
	 * 构建 Excel 读取器
	 *
	 * @param headRowNumber  标题行数
	 * @param ignoreEmptyRow 忽略空行
	 * @return Excel 读取器实例
	 */
	@Override
	public ExcelReader build(int headRowNumber, boolean ignoreEmptyRow) {
		return new EasyExcelReader(EasyExcel.read()
			.registerConverter(LocalDateConverter.INSTANCE)
			.registerConverter(LocalDateTimeConverter.INSTANCE)
			.headRowNumber(headRowNumber)
			.ignoreEmptyRow(ignoreEmptyRow));
	}
}
