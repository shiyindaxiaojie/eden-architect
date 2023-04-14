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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.cat.spring.boot.env.CatProperties;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.integration.cat.integration.rest.RestTemplateCatInterceptor;

import java.util.Collections;

/**
 * Rest 集成 CAT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnExpression("${cat.http.enabled:true}")
@ConditionalOnBean(CatAutoConfiguration.class)
@AutoConfigureAfter(CatAutoConfiguration.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class RestCatAutoConfiguration implements InitializingBean {

	private static final String AUTOWIRED_REST_TEMPLATE_CAT_INTERCEPTOR = "Autowired RestTemplateCatInterceptor";

	private final RestTemplate restTemplate;

	private final CatProperties catProperties;

	@Override
	public void afterPropertiesSet() {
		log.debug(AUTOWIRED_REST_TEMPLATE_CAT_INTERCEPTOR);
		RestTemplateCatInterceptor interceptor = new RestTemplateCatInterceptor();
		if (StringUtils.isNotBlank(catProperties.getHttp().getIncludeHeaders())) {
			interceptor.setIncludeHeaders(catProperties.getHttp().getIncludeHeaders());
		}
		if (catProperties.getHttp().isIncludeBody()) {
			interceptor.setIncludeBody(true);
		}
		restTemplate.setInterceptors(Collections.singletonList(interceptor));
	}
}
