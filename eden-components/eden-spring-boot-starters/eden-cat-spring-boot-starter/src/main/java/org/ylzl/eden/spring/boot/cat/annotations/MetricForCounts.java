package org.ylzl.eden.spring.boot.cat.annotations;

import java.lang.annotation.*;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MetricForCounts {

	MetricForCount[] value();
}
