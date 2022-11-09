package org.ylzl.eden.idempotent.core;

import org.ylzl.eden.commons.lang.StringConstants;

import java.lang.annotation.*;

/**
 * 幂等性标注
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Idempotent {

	String key() default StringConstants.EMPTY;

	int expireInSeconds() default 1;

	boolean deleteAfterInvoke() default false;
}
