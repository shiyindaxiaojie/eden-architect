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

package org.ylzl.eden.zipkin2.spring.cloud.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.eden.spring.cloud.sleuth.web.WebMvcHandlerParser;
import org.ylzl.eden.zipkin2.spring.cloud.env.CustomSleuthWebProperties;

/**
 * Sleuth Web 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnExpression("${spring.zipkin.enabled:false}")
@ConditionalOnProperty(
	prefix = "spring.sleuth",
	name = "web.servlet.enabled",
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(CustomSleuthWebProperties.class)
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class SleuthWebAutoConfiguration {

	public static final String AUTOWIRED_WEB_MVC_HANDLER_PARSER = "Autowired WebMvcHandlerParser";

	@Bean
	public WebMvcHandlerParser webMvcHandlerParser(CustomSleuthWebProperties customSleuthWebProperties) {
		log.debug(AUTOWIRED_WEB_MVC_HANDLER_PARSER);
		WebMvcHandlerParser webMvcHandlerParser = new WebMvcHandlerParser();
		if (customSleuthWebProperties.getIgnoreHeaders() != null) {
			webMvcHandlerParser.setIgnoreHeaders(customSleuthWebProperties.getIgnoreHeaders());
		}
		if (customSleuthWebProperties.getIgnoreParameters() != null) {
			webMvcHandlerParser.setIgnoreParameters(customSleuthWebProperties.getIgnoreParameters());
		}
		return webMvcHandlerParser;
	}
}
