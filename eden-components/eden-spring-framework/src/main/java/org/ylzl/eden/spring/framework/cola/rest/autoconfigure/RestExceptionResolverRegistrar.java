package org.ylzl.eden.spring.framework.cola.rest.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.spring.framework.cola.rest.resolver.RestExceptionResolver;

/**
 * RestExceptionResolver 注册器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class RestExceptionResolverRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(@NotNull AnnotationMetadata annotationMetadata,
										@NotNull BeanDefinitionRegistry beanDefinitionRegistry) {
		BeanDefinitionBuilder.rootBeanDefinition(RestExceptionResolver.class);
	}
}
