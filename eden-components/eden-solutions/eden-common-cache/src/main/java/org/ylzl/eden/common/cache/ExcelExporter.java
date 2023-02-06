package org.ylzl.eden.dynamic.excel;

import org.ylzl.eden.dynamic.excel.sheet.Sheet;

import java.lang.annotation.*;

/**
 * Excel 导出
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcelExporter {

	/**
	 * 文件名称
	 *
	 * @return 文件名称
	 */
	String fileName() default "excel";

	/**
	 * 设置密码
	 *
	 * @return 设置密码
	 */
	String password() default "";

	/**
	 * 工作表
	 *
	 * @return 工作表
	 */
	Sheet[] sheets() default @Sheet(sheetName = "sheet1");

	/**
	 * 是否在内存操作
	 *
	 * @return 是否在内存操作
	 */
	boolean inMemory() default false;

	/**
	 * 使用模板
	 *
	 * @return 模板位置
	 */
	String template() default "";

	/**
	 * 包含的字段
	 *
	 * @return String[]
	 */
	String[] include() default {};

	/**
	 * 排除的字段
	 *
	 * @return String[]
	 */
	String[] exclude() default {};
}
