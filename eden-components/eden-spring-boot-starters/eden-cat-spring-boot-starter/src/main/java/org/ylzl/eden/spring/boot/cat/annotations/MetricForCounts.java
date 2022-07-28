package org.ylzl.eden.spring.boot.cat.annotations;

import java.lang.annotation.*;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MetricForCounts {

	MetricForCount[] value();
}
