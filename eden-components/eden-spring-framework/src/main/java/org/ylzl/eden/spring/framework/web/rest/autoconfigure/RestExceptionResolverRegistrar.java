package org.ylzl.eden.spring.framework.web.rest.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.web.rest.resolver.RestExceptionResolver;

/**
 * RestExceptionResolver 注册器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class RestExceptionResolverRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(@NotNull AnnotationMetadata metadata,
										@NotNull BeanDefinitionRegistry registry) {
		ApplicationContextHelper.registerBean(RestExceptionResolver.class, registry);
	}
}
