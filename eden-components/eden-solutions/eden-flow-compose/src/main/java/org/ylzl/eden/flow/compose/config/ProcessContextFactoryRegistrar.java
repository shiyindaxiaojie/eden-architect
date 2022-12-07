package org.ylzl.eden.flow.compose.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.flow.compose.parser.ClassPathXmlProcessParser;
import org.ylzl.eden.flow.compose.context.ProcessContextFactory;

import java.util.Objects;

/**
 * ProcessContextFactory 注册器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class ProcessContextFactoryRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata,
										@NotNull BeanDefinitionRegistry registry) {
		String classPathXml = (String) Objects.requireNonNull(metadata.getAnnotationAttributes(
			EnableFlowCompose.class.getName())).get("value");
		ClassPathXmlProcessParser parser = new ClassPathXmlProcessParser(classPathXml);
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(ProcessContextFactory.class);
		bdb.addConstructorArgReference(ProcessFactoryConfiguration.SPRING_BEAN_PROCESSOR_FACTORY_NAME);
		bdb.addConstructorArgValue(parser);
		registry.registerBeanDefinition(ProcessContextFactory.class.getName(), bdb.getBeanDefinition());
	}
}
