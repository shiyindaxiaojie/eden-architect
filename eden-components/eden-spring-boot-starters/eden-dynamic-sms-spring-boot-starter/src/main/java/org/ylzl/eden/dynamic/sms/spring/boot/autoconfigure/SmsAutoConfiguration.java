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

package org.ylzl.eden.dynamic.sms.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.dynamic.sms.SmsTemplate;
import org.ylzl.eden.dynamic.sms.spring.boot.env.SmsProperties;
import org.ylzl.eden.dynamic.sms.spring.boot.support.SmsHelper;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * 短信操作模板策略自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = SmsProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE
)
@ConditionalOnBean(SmsTemplate.class)
@EnableConfigurationProperties(SmsProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SmsAutoConfiguration {

	public static final String AUTOWIRED_SMS_BEAN_FACTORY = "Autowired SmsBeanFactory";

	private final SmsProperties smsProperties;

	@Bean
	public SmsHelper smsHelper() {
		log.debug(AUTOWIRED_SMS_BEAN_FACTORY);
		return new SmsHelper(smsProperties.getPrimary());
	}
}
