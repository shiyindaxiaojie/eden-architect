package org.ylzl.eden.spring.boot.cat.autoconfigure;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.spring.boot.cat.annotations.CatTransactionAnnotationProcessor;
import org.ylzl.eden.spring.boot.cat.annotations.MetricForCountAnnotationProcessor;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
public class AnnotationProcessorRegister implements ImportBeanDefinitionRegistrar {

	private final BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
		register(MetricForCountAnnotationProcessor.class, registry);
		register(CatTransactionAnnotationProcessor.class, registry);
	}

	private void register(Class<?> beanClass, BeanDefinitionRegistry registry) {
		BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
		String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
		registry.registerBeanDefinition(beanName, beanDefinition);
	}
}
