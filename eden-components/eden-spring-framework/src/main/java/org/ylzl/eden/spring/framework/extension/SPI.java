package org.ylzl.eden.spring.framework.extension;

import java.lang.annotation.*;

/**
 * 扩展点接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

	String value() default "";
}
