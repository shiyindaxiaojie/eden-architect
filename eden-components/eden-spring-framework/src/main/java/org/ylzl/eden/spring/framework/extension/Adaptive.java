package org.ylzl.eden.spring.framework.extension;

import java.lang.annotation.*;

/**
 * 默认扩展实现标记
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Adaptive {

	String[] value() default {};
}
