package org.ylzl.eden.event.auditor;

import java.lang.annotation.*;

/**
 * 支持多个事件审计
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EventAuditors {

	EventAuditor[] value();
}
