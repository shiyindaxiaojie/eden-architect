package org.ylzl.eden.spring.integration.bpc.config.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.integration.bpc.executor.factory.SpringBeanProcessorFactory;

/**
 * 流程工厂自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
@Configuration
public class ProcessFactoryAutoConfiguration {

	public static final String SPRING_BEAN_PROCESSOR_FACTORY_NAME = "springBeanProcessorFactory";

	private static final String AUTOWIRED_SPRING_BEAN_PROCESSOR_FACTORY = "Autowired SpringBeanProcessorFactory";

	@ConditionalOnMissingBean
	@Bean
	public SpringBeanProcessorFactory springBeanProcessorFactory() {
		log.debug(AUTOWIRED_SPRING_BEAN_PROCESSOR_FACTORY);
		return new SpringBeanProcessorFactory();
	}
}
