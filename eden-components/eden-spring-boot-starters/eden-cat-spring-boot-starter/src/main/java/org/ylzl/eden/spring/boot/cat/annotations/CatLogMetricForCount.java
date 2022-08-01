package org.ylzl.eden.spring.boot.cat.annotations;

import java.lang.annotation.*;

/**
 * Cat.logMetricForCount 注解
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CatLogMetricForCount {

	/**
	 * 指标名称
	 *
	 * @return
	 */
	String name() default "";

	/**
	 * 调用计数
	 */
	int count() default 1;
}
