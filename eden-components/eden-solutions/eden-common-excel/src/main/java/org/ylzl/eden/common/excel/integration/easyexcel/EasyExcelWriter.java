package org.ylzl.eden.common.excel.integration.easyexcel;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ylzl.eden.common.excel.ExcelWriter;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

/**
 * EasyExcel 写入 Excel
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Data
public class EasyExcelWriter implements ExcelWriter {

	private final ExcelWriterBuilder excelWriterBuilder;

	/**
	 * 写入 Excel
	 *
	 * @param outputStream 输出流
	 * @param data 填充数据
	 * @param head 标题
	 */
	@Override
	public void write(OutputStream outputStream, List<Object> data, Class<?> head) {
		excelWriterBuilder.file(outputStream)
			.autoCloseStream(true)
			.head(head)
			.sheet()
			.doWrite(data);
	}

	/**
	 * 写入 Excel
	 *
	 * @param file 文件
	 * @param data 填充数据
	 * @param head 标题
	 */
	@Override
	public void write(File file, List<Object> data, Class<?> head) {
		excelWriterBuilder.file(file)
			.autoCloseStream(true)
			.head(head)
			.sheet()
			.doWrite(data);
	}
}
