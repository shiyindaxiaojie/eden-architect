package org.ylzl.eden.extension;

import java.lang.annotation.*;

import static org.ylzl.eden.extension.Inject.InjectType.ByName;

/**
 * 注入类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Inject {

	boolean enable() default true;

	InjectType type() default ByName;

	enum InjectType{
		ByName,
		ByType
	}
}
