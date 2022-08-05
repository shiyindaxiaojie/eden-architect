package org.ylzl.eden.spring.boot.cat.autoconfigure;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.spring.boot.cat.annotations.CatTransactionAnnotationProcessor;
import org.ylzl.eden.spring.boot.cat.annotations.CatLogMetricForCountAnnotationProcessor;

/**
 * CAT 注解处理器注册
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class AnnotationProcessorRegister implements ImportBeanDefinitionRegistrar {

	private final BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

	@Override
	public void registerBeanDefinitions(@NotNull AnnotationMetadata metadata, @NotNull BeanDefinitionRegistry registry) {
		register(CatLogMetricForCountAnnotationProcessor.class, registry);
		register(CatTransactionAnnotationProcessor.class, registry);
	}

	private void register(Class<?> beanClass, BeanDefinitionRegistry registry) {
		BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
		String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
		registry.registerBeanDefinition(beanName, beanDefinition);
	}
}
