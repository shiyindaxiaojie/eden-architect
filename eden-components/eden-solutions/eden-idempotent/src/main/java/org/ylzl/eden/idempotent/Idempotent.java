package org.ylzl.eden.idempotent;

import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.idempotent.strategy.IdempotentStrategy;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

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

	IdempotentStrategy strategy() default IdempotentStrategy.TTL;

	String key() default Strings.EMPTY;

	long ttl() default 10L;

	TimeUnit timeUnit() default TimeUnit.SECONDS;
}
