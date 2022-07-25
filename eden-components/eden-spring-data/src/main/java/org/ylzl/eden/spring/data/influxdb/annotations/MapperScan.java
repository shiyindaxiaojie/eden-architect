package org.ylzl.eden.spring.data.influxdb.annotations;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MapperScannerRegistrar.class)
@Repeatable(MapperScans.class)
public @interface MapperScan {

	String[] value() default {};

	String[] basePackages() default {};

	Class<?>[] basePackageClasses() default {};
}
