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

package org.ylzl.eden.spring.integration.cat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.ylzl.eden.spring.integration.cat.EnableCat;
import org.ylzl.eden.spring.integration.cat.aop.CatLogMetricForCountInterceptor;
import org.ylzl.eden.spring.integration.cat.aop.CatLogMetricForCountPointcutAdvisor;
import org.ylzl.eden.spring.integration.cat.aop.CatTransactionMethodInterceptor;

/**
 * Cat 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class CatConfiguration implements ImportAware {

	public static final String IMPORTING_META_NOT_FOUND = "@EnableCat is not present on importing class";

	private AnnotationAttributes enableCat;

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableCat = AnnotationAttributes.fromMap(
			importMetadata.getAnnotationAttributes(EnableCat.class.getName(), false));
		if (this.enableCat == null) {
			log.warn(IMPORTING_META_NOT_FOUND);
		}
	}

	@Bean
	public CatLogMetricForCountInterceptor catLogMetricForCountInterceptor() {
		return new CatLogMetricForCountInterceptor();
	}

	@Bean
	public CatLogMetricForCountPointcutAdvisor CatLogMetricForCountPointcutAdvisor(
		CatLogMetricForCountInterceptor CatLogMetricForCountInterceptor) {
		CatLogMetricForCountPointcutAdvisor pointcutAdvisor = new CatLogMetricForCountPointcutAdvisor();
		pointcutAdvisor.setAdviceBeanName("CatLogMetricForCountPointcutAdvisor");
		pointcutAdvisor.setAdvice(CatLogMetricForCountInterceptor);
		if (enableCat != null) {
			pointcutAdvisor.setOrder(enableCat.getNumber("order"));
		}
		return pointcutAdvisor;
	}

	@Bean
	public CatTransactionMethodInterceptor catTransactionMethodInterceptor() {
		return new CatTransactionMethodInterceptor();
	}
}
