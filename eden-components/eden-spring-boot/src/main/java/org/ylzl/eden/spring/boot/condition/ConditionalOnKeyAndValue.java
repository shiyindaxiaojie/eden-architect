package org.ylzl.eden.spring.boot.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 简易 key-value 匹配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(KeyAndValueCondition.class)
public @interface ConditionalOnKeyAndValue {

	String key();

	String value();
}
