/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.boot.web.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.commons.env.Charsets;

import java.util.List;

/**
 * RestTemplate 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(RestTemplateAutoConfiguration.class)
@ConditionalOnClass({RestTemplate.class})
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class EnhancedRestTemplateAutoConfiguration implements InitializingBean {

	private static final String INITIALIZING_REST_TEMPLATE = "Initializing RestTemplate";

	private final RestTemplate restTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug(INITIALIZING_REST_TEMPLATE);
		this.setDefaultCharset(restTemplate.getMessageConverters());
	}

	private void setDefaultCharset(List<HttpMessageConverter<?>> httpMessageConverters) {
		for (HttpMessageConverter<?> httpMessageConverter : httpMessageConverters) {
			if (httpMessageConverter instanceof StringHttpMessageConverter) {
				((StringHttpMessageConverter) httpMessageConverter)
					.setDefaultCharset(Charsets.UTF_8);
				break;
			}
		}
	}

	@ConditionalOnMissingBean(RestTemplate.class)
	@Configuration(proxyBeanMethods = false)
	public static class MissingRestTemplateAutoConfiguration {

		private static final String AUTOWIRED_REST_TEMPLATE = "Autowired RestTemplate";

		@Bean
		public RestTemplate restTemplate() {
			log.debug(AUTOWIRED_REST_TEMPLATE);
			return new RestTemplate();
		}
	}
}
