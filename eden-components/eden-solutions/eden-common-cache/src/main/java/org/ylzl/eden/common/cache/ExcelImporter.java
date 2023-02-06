package org.ylzl.eden.dynamic.excel;

import java.lang.annotation.*;

/**
 * Excel 导入
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface ExcelImporter {

	/**
	 * 是否跳过空行
	 *
	 * @return 是否跳过空行
	 */
	boolean skipEmptyRow() default true;
}
