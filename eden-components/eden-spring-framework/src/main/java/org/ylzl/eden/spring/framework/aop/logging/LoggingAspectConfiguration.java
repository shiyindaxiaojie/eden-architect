/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.framework.aop.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.PathMatcher;

/**
 * 日志切面配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class LoggingAspectConfiguration implements ImportAware {

	private static final String IMPORTING_META_NOT_FOUND = "@EnableLoggingAspect is not present on importing class";

	private static final String BEAN_NAME = "loggingAspectPointcutAdvisor";

	private AnnotationAttributes enableLoggingAspect;

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableLoggingAspect = AnnotationAttributes.fromMap(
			importMetadata.getAnnotationAttributes(EnableLoggingAspect.class.getName(), false));
		if (this.enableLoggingAspect == null) {
			log.warn(IMPORTING_META_NOT_FOUND);
		}
	}

	@Bean
	public LoggingAspectInterceptor loggingAspectInterceptor(ObjectProvider<LoggingAspectConfig> loggingAspectConfig) {
		return new LoggingAspectInterceptor(getLoggingAspectConfig(loggingAspectConfig));
	}

	@Bean
	public LoggingAspectPointcutAdvisor loggingAspectPointcutAdvisor(LoggingAspectInterceptor loggingAspectInterceptor,
		PathMatcher pathMatcher, ObjectProvider<LoggingAspectConfig> loggingAspectConfig) {
		LoggingAspectPointcutAdvisor pointcutAdvisor = new LoggingAspectPointcutAdvisor(pathMatcher,
			getLoggingAspectConfig(loggingAspectConfig).getPackages());
		pointcutAdvisor.setAdviceBeanName(BEAN_NAME);
		pointcutAdvisor.setAdvice(loggingAspectInterceptor);
		if (enableLoggingAspect != null) {
			pointcutAdvisor.setOrder(enableLoggingAspect.getNumber("order"));
		}
		return pointcutAdvisor;
	}

	private LoggingAspectConfig getLoggingAspectConfig(ObjectProvider<LoggingAspectConfig> loggingAspectConfig) {
		LoggingAspectConfig config = new LoggingAspectConfig();
		config.setPackages(enableLoggingAspect.getStringArray("packages"));
		return loggingAspectConfig.getIfUnique(() -> config);
	}
}
