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
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate 转换器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public enum LocalDateConverter implements Converter<LocalDate> {

	INSTANCE;

	@Override
	public Class<?> supportJavaTypeKey() {
		return LocalDate.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public LocalDate convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
									   GlobalConfiguration globalConfiguration) {
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			return LocalDate.parse(cellData.getStringValue());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
			contentProperty.getDateTimeFormatProperty().getFormat());
		return LocalDate.parse(cellData.getStringValue(), formatter);
	}

	@Override
	public WriteCellData<?> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty,
											   GlobalConfiguration globalConfiguration) {
		DateTimeFormatter formatter = contentProperty == null || contentProperty.getDateTimeFormatProperty() == null ?
			DateTimeFormatter.ISO_LOCAL_DATE :
			DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
		return new WriteCellData<>(value.format(formatter));
	}
}
