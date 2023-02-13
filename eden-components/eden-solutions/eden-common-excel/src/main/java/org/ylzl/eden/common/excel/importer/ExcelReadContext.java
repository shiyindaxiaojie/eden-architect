package org.ylzl.eden.common.excel.importer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Excel 读取上下文
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExcelReadContext {

	private boolean readExcelLine = false;
}
