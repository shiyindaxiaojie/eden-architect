package org.ylzl.eden.spring.integration.bpc.config.annotation;

import org.springframework.context.annotation.Import;
import org.ylzl.eden.spring.integration.bpc.config.autoconfigure.ProcessFactoryAutoConfiguration;

import java.lang.annotation.*;

/**
 * 开启业务流程引擎
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ProcessFactoryAutoConfiguration.class, ProcessFactoryAutoConfiguration.class})
public @interface EnableBusinessProcessEngine {

	/**
	 * 配置文件
	 */
	String value();
}
