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

package org.ylzl.eden.cat.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.spring.integration.cat.integration.web.filter.CatHttpTraceFilter;

/**
 * Web 集成 CAT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnBean(CatAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class WebCatAutoConfiguration {

	public static final String AUTOWIRED_CAT_HTTP_TRACE_FILTER_FILTER = "Autowired CatHttpTraceFilterFilter";

	@Bean
	public FilterRegistrationBean<CatHttpTraceFilter> catHttpTraceFilterFilter() {
		log.debug(AUTOWIRED_CAT_HTTP_TRACE_FILTER_FILTER);
		FilterRegistrationBean<CatHttpTraceFilter> registration =
			new FilterRegistrationBean<>(new CatHttpTraceFilter());
		registration.setName("http-trace-cat-filter");
		registration.addUrlPatterns("/*");
		registration.setOrder(1);
		return registration;
	}
}
