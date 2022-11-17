package org.ylzl.eden.cola.extension;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 扩展点（多个）
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface Extensions {

	String[] bizId() default BizScenario.DEFAULT_BIZ_ID;

	String[] useCase() default BizScenario.DEFAULT_USE_CASE;

	String[] scenario() default BizScenario.DEFAULT_SCENARIO;

	Extension[] value() default {};
}
