package org.ylzl.eden.spring.integration.cat.annotations;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;

/**
 * CAT 注解处理器注册
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CatAnnotationProcessorRegister implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(@NotNull AnnotationMetadata metadata, @NotNull BeanDefinitionRegistry registry) {
		ApplicationContextHelper.registerBean(CatLogMetricForCountAnnotationProcessor.class, registry);
		ApplicationContextHelper.registerBean(CatTransactionAnnotationProcessor.class, registry);
	}
}
