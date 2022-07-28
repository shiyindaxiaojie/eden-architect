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
@Repeatable(Update.List.class)
public @interface Update {

	String[] value();

	String databaseId() default "";

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface List {
		Update[] value();
	}
}
