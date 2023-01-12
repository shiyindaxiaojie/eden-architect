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

package org.ylzl.eden.dynamic.mail.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.mail.javamail.JavaMailSender;
import org.ylzl.eden.dynamic.mail.MailTemplate;
import org.ylzl.eden.dynamic.mail.integration.javamail.JavaMailTemplate;
import org.ylzl.eden.dynamic.mail.spring.boot.env.MailProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * JavaMailTemplate 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = MailProperties.JavaMail.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@Conditional(JavaMailAutoConfiguration.MailSenderCondition.class)
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class JavaMailAutoConfiguration {

	public static final String AUTOWIRED_JAVA_MAIL_TEMPLATE = "Autowired JavaMailTemplate";

	private final JavaMailSender javaMailSender;

	@Bean
	public MailTemplate mailTemplate() {
		log.debug(AUTOWIRED_JAVA_MAIL_TEMPLATE);
		return new JavaMailTemplate(javaMailSender);
	}

	static class MailSenderCondition extends AnyNestedCondition {

		MailSenderCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnProperty(prefix = "spring.mail", name = "host")
		static class HostProperty {}

		@ConditionalOnProperty(prefix = "spring.mail", name = "jndi-name")
		static class JndiNameProperty {}
	}
}
