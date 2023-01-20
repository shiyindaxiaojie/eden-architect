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

package org.ylzl.eden.event.auditor.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.event.auditor.EnableEventAuditor;
import org.ylzl.eden.event.auditor.spring.boot.env.EventAuditorProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.eden.spring.boot.task.autoconfigure.AsyncTaskExecutionAutoConfiguration;

/**
 * 事件审计自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnProperty(
    prefix = EventAuditorProperties.PREFIX,
    name = Conditions.ENABLED,
    havingValue = Conditions.TRUE,
    matchIfMissing = true
)
@AutoConfigureAfter(AsyncTaskExecutionAutoConfiguration.class)
@EnableEventAuditor
@EnableConfigurationProperties(EventAuditorProperties.class)
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class EventAuditorAutoConfiguration {

}
