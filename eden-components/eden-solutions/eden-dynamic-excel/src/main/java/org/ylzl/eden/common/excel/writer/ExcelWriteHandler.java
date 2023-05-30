package org.ylzl.eden.common.excel.writer;

import java.io.OutputStream;
import java.util.List;

/**
 * Excel 生成处理器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface ExcelWriteHandler {

	/**
	 * 返回 Excel
	 *
	 * @param data 填充数据
	 * @param os   输出流
	 */
	void write(List<Object> data, OutputStream os);
}
