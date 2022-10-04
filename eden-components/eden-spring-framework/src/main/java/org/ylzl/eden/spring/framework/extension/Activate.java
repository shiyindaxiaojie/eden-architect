package org.ylzl.eden.spring.framework.extension;

import java.lang.annotation.*;

/**
 * 激活扩展点
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Activate {

	String[] group() default {};

	String[] value() default {};

	int order() default 0;
}
