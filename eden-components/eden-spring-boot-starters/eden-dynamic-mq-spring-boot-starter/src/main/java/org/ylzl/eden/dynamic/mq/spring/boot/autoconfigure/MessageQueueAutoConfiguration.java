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

package org.ylzl.eden.dynamic.mq.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.dynamic.mq.spring.boot.autoconfigure.condition.ConditionalOnMessageQueue;
import org.ylzl.eden.dynamic.mq.spring.boot.env.MessageQueueProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.support.MessageQueueHelper;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * 消息队列自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = MessageQueueProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnMessageQueue
@EnableConfigurationProperties(MessageQueueProperties.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class MessageQueueAutoConfiguration {

	public static final String AUTOWIRED_MESSAGE_QUEUE_HELPER = "Autowired MessageQueueHelper";

	private final MessageQueueProperties messageQueueProperties;

	@Bean
	public MessageQueueHelper messageQueueHelper() {
		log.debug(AUTOWIRED_MESSAGE_QUEUE_HELPER);
		return new MessageQueueHelper(messageQueueProperties.getPrimary());
	}
}
