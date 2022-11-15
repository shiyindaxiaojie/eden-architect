package org.ylzl.eden.extension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 扩展点包装器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Wrapper {

	String[] matches() default {};

	String[] mismatches() default {};
}
