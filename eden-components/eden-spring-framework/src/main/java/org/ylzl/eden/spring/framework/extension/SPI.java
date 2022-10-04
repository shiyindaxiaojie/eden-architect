package org.ylzl.eden.spring.framework.extension;

import java.lang.annotation.*;

/**
 * 扩展点接口
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

	String value() default "";
}
