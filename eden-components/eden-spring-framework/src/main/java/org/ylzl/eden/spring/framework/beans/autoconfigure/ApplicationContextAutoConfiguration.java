package org.ylzl.eden.spring.framework.beans.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;

/**
 * ApplicationContext 自动装配
 *
 * @author gyl
 * @since 2.4.x
 */
@Slf4j
@Configuration
public class ApplicationContextAutoConfiguration {

	@ConditionalOnMissingBean(ApplicationContextHelper.class)
	@Bean
	public ApplicationContextHelper applicationContextHelper() {
		log.debug("Autowired ApplicationContextHelper");
		return new ApplicationContextHelper();
	}
}
