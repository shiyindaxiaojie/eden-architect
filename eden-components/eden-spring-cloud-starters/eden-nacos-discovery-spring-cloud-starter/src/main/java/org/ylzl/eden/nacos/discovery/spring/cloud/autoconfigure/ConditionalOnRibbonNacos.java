package org.ylzl.eden.nacos.discovery.spring.cloud.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ConditionalOnRibbonNacos
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnProperty(
	prefix = "ribbon",
	name = "nacos.enabled",
	havingValue = Conditions.ENABLED,
	matchIfMissing = true
)
public @interface ConditionalOnRibbonNacos {
}
