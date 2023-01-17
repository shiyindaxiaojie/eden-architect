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

package org.ylzl.eden.event.auditor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.event.auditor.EnableEventAuditor;
import org.ylzl.eden.event.auditor.aop.EventAuditorInterceptor;
import org.ylzl.eden.event.auditor.aop.EventAuditorPointcutAdvisor;
import org.ylzl.eden.spring.framework.expression.function.CustomFunctionRegistrar;

/**
 * 事件审计配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
@Role(BeanDefinition.ROLE_APPLICATION)
@Configuration(proxyBeanMethods = false)
public class EventAuditorConfiguration implements ImportAware {

	public static final String IMPORTING_META_NOT_FOUND = "@EnableEventAuditor is not present on importing class";

	private AnnotationAttributes enableEventAuditor;

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableEventAuditor = AnnotationAttributes.fromMap(
			importMetadata.getAnnotationAttributes(EnableEventAuditor.class.getName(), false));
		if (this.enableEventAuditor == null) {
			log.warn(IMPORTING_META_NOT_FOUND);
		}
	}

	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean
	public EventAuditorInterceptor eventAuditorInterceptor(ObjectProvider<EventAuditorConfig> eventAuditorConfig) {
		return new EventAuditorInterceptor(eventAuditorConfig.getIfUnique(EventAuditorConfig::new));
	}

	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean
	public EventAuditorPointcutAdvisor eventAuditorPointcutAdvisor(EventAuditorInterceptor eventAuditorInterceptor) {
		EventAuditorPointcutAdvisor pointcutAdvisor = new EventAuditorPointcutAdvisor();
		pointcutAdvisor.setAdviceBeanName("eventAuditorPointcutAdvisor");
		pointcutAdvisor.setAdvice(eventAuditorInterceptor);
		pointcutAdvisor.setOrder(enableEventAuditor.getNumber("order"));
		return pointcutAdvisor;
	}

	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean
	public CustomFunctionRegistrar customFunctionRegistrar() {
		return new CustomFunctionRegistrar();
	}
}
