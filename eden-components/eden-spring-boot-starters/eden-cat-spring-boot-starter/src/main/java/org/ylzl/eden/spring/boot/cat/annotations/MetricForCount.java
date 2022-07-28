package org.ylzl.eden.spring.boot.cat.annotations;

import java.lang.annotation.*;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MetricForCount {

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
