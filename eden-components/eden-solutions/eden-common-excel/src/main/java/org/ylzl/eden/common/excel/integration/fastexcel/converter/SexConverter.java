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

package org.ylzl.eden.common.excel.integration.fastexcel.converter;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.converters.ReadConverterContext;
import cn.idev.excel.converters.WriteConverterContext;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

/**
 * 性别转换器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SexConverter implements Converter<Integer> {

	public static final String MALE = "男";

	public static final String FEMALE = "女";

	@Override
	public Class<Integer> supportJavaTypeKey() {
		return Integer.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
									 GlobalConfiguration globalConfiguration) {
		if (MALE.equals(cellData.getStringValue())) {
			return 1;
		}
		if (FEMALE.equals(cellData.getStringValue())) {
			return 0;
		}
		return -1;
	}

	@Override
	public Integer convertToJavaData(ReadConverterContext<?> context) {
		if (MALE.equals(context.getReadCellData().getStringValue())) {
			return 1;
		}
		if (FEMALE.equals(context.getReadCellData().getStringValue())) {
			return 0;
		}
		return -1;
	}

	@Override
	public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
		if (context.getValue() == 1) {
			return new WriteCellData<>(MALE);
		}
		if (context.getValue() == 0) {
			return new WriteCellData<>(FEMALE);
		}
		return new WriteCellData<>("?");
	}
}
