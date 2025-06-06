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

package org.ylzl.eden.common.excel.integration.fastexcel;

import cn.idev.excel.FastExcel;
import cn.idev.excel.support.ExcelTypeEnum;
import org.ylzl.eden.common.excel.ExcelType;
import org.ylzl.eden.common.excel.ExcelWriter;
import org.ylzl.eden.common.excel.builder.AbstractExcelWriterBuilder;
import org.ylzl.eden.common.excel.builder.ExcelWriterBuilder;
import org.ylzl.eden.common.excel.integration.fastexcel.converter.LocalDateConverter;
import org.ylzl.eden.common.excel.integration.fastexcel.converter.LocalDateTimeConverter;

/**
 * EasyExcel 写入 Excel
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class FastExcelWriterBuilder extends AbstractExcelWriterBuilder implements ExcelWriterBuilder {

	/**
	 * 构建 Excel 写入器
	 *
	 * @param excelType 文件类型
	 * @param inMemory  是否在内存操作
	 * @return Excel 写入器实例
	 */
	@Override
	public ExcelWriter build(ExcelType excelType, boolean inMemory) {
		return new FastExcelWriter(FastExcel.write()
			.registerConverter(LocalDateConverter.INSTANCE)
			.registerConverter(LocalDateTimeConverter.INSTANCE)
			.excelType(this.valueOf(excelType))
			.inMemory(inMemory));
	}

	/**
	 * 枚举转换
	 *
	 * @param excelType 文件类型
	 * @return EasyExcel 枚举
	 */
	private ExcelTypeEnum valueOf(ExcelType excelType) {
		switch (excelType) {
			case CSV:
				return ExcelTypeEnum.CSV;
			case XLS:
				return ExcelTypeEnum.XLS;
		}
		return ExcelTypeEnum.XLSX;
	}
}
