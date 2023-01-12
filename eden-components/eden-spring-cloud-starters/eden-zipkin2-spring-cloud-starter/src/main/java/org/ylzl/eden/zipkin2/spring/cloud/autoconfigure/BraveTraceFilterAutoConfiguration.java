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

import brave.dubbo.TracingFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * 自定义 Brave TraceFilter 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = "dubbo",
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@AutoConfigureAfter(DubboAutoConfiguration.class)
@ConditionalOnClass(TracingFilter.class)
@ConditionalOnBean({ProviderConfig.class, ConsumerConfig.class})
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class BraveTraceFilterAutoConfiguration implements InitializingBean {

	public static final String TRACING = "tracing";

	public static final String ADD_TRACING_FILTER = "Initializing providerConfig and consumerConfig add tracing filter";

	private final ProviderConfig providerConfig;

	private final ConsumerConfig consumerConfig;

	@Override
	public void afterPropertiesSet() {
		log.debug(ADD_TRACING_FILTER);
		providerConfig.setFilter(TRACING);
		consumerConfig.setFilter(TRACING);
	}
}
