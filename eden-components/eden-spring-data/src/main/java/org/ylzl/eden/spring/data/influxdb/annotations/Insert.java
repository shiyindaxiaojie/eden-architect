package org.ylzl.eden.spring.data.influxdb.annotations;

import java.lang.annotation.*;

/**
 * Delete 删除
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Insert.List.class)
public @interface Insert {

	String[] value();

	String databaseId() default "";

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface List {
		Insert[] value();
	}
}
