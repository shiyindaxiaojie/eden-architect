package org.ylzl.eden.common.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 性别转换器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SexConverter implements Converter<Integer> {

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
		return "男".equals(cellData.getStringValue()) ? 1 : 0;
	}

	@Override
	public Integer convertToJavaData(ReadConverterContext<?> context) {
		return "男".equals(context.getReadCellData().getStringValue()) ? 1 : 0;
	}

	@Override
	public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
		return new WriteCellData<>(context.getValue() == 1? "男" : "女");
	}
}
