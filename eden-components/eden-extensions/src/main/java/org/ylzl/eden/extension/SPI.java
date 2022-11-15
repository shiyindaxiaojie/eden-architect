package org.ylzl.eden.extension;

import java.lang.annotation.*;

/**
 * 扩展点标记
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
