package org.ylzl.eden.common.excel.writer.sheet;

import java.lang.annotation.*;

/**
 * 工作表
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Sheet {

	/**
	 * 序号
	 *
	 * @return 序号
	 */
	int sheetNo() default -1;

	/**
	 * 工作表名称
	 *
	 * @return 名称
	 */
	String sheetName();

	/**
	 * 包含字段
	 *
	 * @return 字段
	 */
	String[] includes() default {};

	/**
	 * 排除字段
	 *
	 * @return 字段
	 */
	String[] excludes() default {};
}
