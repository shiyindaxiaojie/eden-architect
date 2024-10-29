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

package org.ylzl.eden.common.mail.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.common.mail.MailTemplate;
import org.ylzl.eden.common.mail.spring.boot.env.MailProperties;
import org.ylzl.eden.common.mail.spring.boot.support.MailHelper;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * 短信操作模板策略自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnExpression("${spring.mail.enabled:true}")
@ConditionalOnProperty(
	prefix = MailProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE
)
@ConditionalOnBean(MailTemplate.class)
@EnableConfigurationProperties(MailProperties.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class MailAutoConfiguration {

	public static final String AUTOWIRED_MAIL_BEAN_FACTORY = "Autowired MailBeanFactory";

	private final MailProperties mailProperties;

	@Bean
	public MailHelper mailBeanFactory() {
		log.debug(AUTOWIRED_MAIL_BEAN_FACTORY);
		return new MailHelper(mailProperties.getPrimary());
	}
}
