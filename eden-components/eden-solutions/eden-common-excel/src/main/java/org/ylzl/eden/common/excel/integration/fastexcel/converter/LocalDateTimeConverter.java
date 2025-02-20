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
import cn.idev.excel.util.DateUtils;
import org.ylzl.eden.commons.lang.Strings;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime 转换器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public enum LocalDateTimeConverter implements Converter<LocalDateTime> {

	INSTANCE;

	@Override
	public Class<?> supportJavaTypeKey() {
		return LocalDateTime.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
										   GlobalConfiguration globalConfiguration) {
		String stringValue = cellData.getStringValue();
		String pattern = contentProperty == null || contentProperty.getDateTimeFormatProperty() == null ?
			switchDateFormat(stringValue) : contentProperty.getDateTimeFormatProperty().getFormat();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(cellData.getStringValue(), formatter);
	}

	@Override
	public WriteCellData<?> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
											   GlobalConfiguration globalConfiguration) {
		String pattern = contentProperty == null || contentProperty.getDateTimeFormatProperty() == null ?
			DateUtils.DATE_FORMAT_19 : contentProperty.getDateTimeFormatProperty().getFormat();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return new WriteCellData<>(value.format(formatter));
	}

	private static String switchDateFormat(String dateString) {
		int length = dateString.length();
		switch (length) {
			case 19:
				if (dateString.contains(Strings.MINUS)) {
					return DateUtils.DATE_FORMAT_19;
				}
				return DateUtils.DATE_FORMAT_19_FORWARD_SLASH;
			case 17:
				return DateUtils.DATE_FORMAT_17;
			case 14:
				return DateUtils.DATE_FORMAT_14;
			case 10:
				return DateUtils.DATE_FORMAT_10;
			default:
				throw new IllegalArgumentException("Can not find date format for: " + dateString);
		}
	}
}
