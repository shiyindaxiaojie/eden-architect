package org.ylzl.eden.spring.framework.expression.function;

import org.springframework.stereotype.Component;
import org.ylzl.eden.commons.lang.Strings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义函数
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Component
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomFunction {

	String value() default Strings.EMPTY;
}
