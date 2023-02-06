package org.ylzl.eden.dynamic.excel.sheet;

import java.lang.annotation.*;

/**
 * 工作表注解
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Sheet {

	int sheetNo() default -1;

	String sheetName();

	String[] includes() default {};

	String[] excludes() default {};
}
