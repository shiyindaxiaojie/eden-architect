package org.ylzl.eden.common.excel.integration.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.ylzl.eden.common.excel.importer.ExcelReadContext;
import org.ylzl.eden.common.excel.importer.ExcelReadListener;
import org.ylzl.eden.common.excel.model.ValidationErrors;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EasyExcelReadListener extends AnalysisEventListener<Object> implements ExcelReadListener<Object> {

	private final List<ValidationErrors> errors = new ArrayList<>();

	private Long rowNumber = 1L;

	/**
	 * 每读取一行调用一次
	 *
	 * @param data    读取到的数据
	 * @param context 读取上下文
	 */
	@Override
	public void read(Object data, ExcelReadContext context) {
		rowNumber++;


	}

	/**
	 * 获取所有校验错误信息
	 *
	 * @return 错误信息清单
	 */
	@Override
	public List<ValidationErrors> getErrors() {
		return null;
	}

	@Override
	public void invoke(Object data, AnalysisContext context) {
		read(data, null);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {

	}
}
