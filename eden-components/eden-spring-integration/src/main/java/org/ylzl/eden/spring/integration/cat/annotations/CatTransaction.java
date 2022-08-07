package org.ylzl.eden.spring.integration.cat.annotations;

import java.lang.annotation.*;

/**
 * Transaction 注解
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CatTransaction {
}
