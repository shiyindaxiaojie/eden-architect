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
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.dynamic.mq.MessageQueueConsumer;
import org.ylzl.eden.dynamic.mq.MessageQueueProvider;
import org.ylzl.eden.dynamic.mq.integration.rocketmq.RocketMQConsumer;
import org.ylzl.eden.dynamic.mq.integration.rocketmq.RocketMQProvider;
import org.ylzl.eden.dynamic.mq.integration.rocketmq.config.RocketMQConfig;
import org.ylzl.eden.dynamic.mq.spring.boot.env.MessageQueueProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.env.RocketMQConsumerProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.env.RocketMQProducerProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.env.convertor.RocketMQConvertor;
import org.ylzl.eden.dynamic.mq.MessageQueueType;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import java.util.List;
import java.util.function.Function;

/**
 * RocketMQ 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = MessageQueueProperties.RocketMQ.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnExpression("${rocketmq.enabled:true}")
@ConditionalOnBean(RocketMQProperties.class)
@ConditionalOnClass(RocketMQTemplate.class)
@AutoConfigureAfter(MessageQueueAutoConfiguration.class)
@EnableConfigurationProperties({
	RocketMQProducerProperties.class,
	RocketMQConsumerProperties.class
})
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class RocketMQMessageQueueAutoConfiguration {

	private static final String AUTOWIRED_ROCKET_MQ_CONSUMER = "Autowired RocketMQConsumer";

	private static final String AUTOWIRED_ROCKET_MQ_PROVIDER = "Autowired RocketMQProvider";

	private final MessageQueueProperties messageQueueProperties;

	private final RocketMQProperties rocketMQProperties;

	@Bean
	public RocketMQConsumer rocketMQConsumer(RocketMQConsumerProperties rocketMQConsumerProperties,
											 ObjectProvider<List<MessageQueueConsumer>> messageListeners) {
		log.debug(AUTOWIRED_ROCKET_MQ_CONSUMER);
		Function<String, Boolean> matcher = type -> StringUtils.isBlank(type) && messageQueueProperties.getPrimary() != null?
			MessageQueueType.ROCKETMQ.name().equalsIgnoreCase(messageQueueProperties.getPrimary()):
			MessageQueueType.ROCKETMQ.name().equalsIgnoreCase(type);
		RocketMQConfig config = RocketMQConvertor.INSTANCE.toConfig(rocketMQProperties);
		RocketMQConvertor.INSTANCE.updateConfigFromConsumer(rocketMQConsumerProperties, config.getConsumer());
		return new RocketMQConsumer(config, messageListeners.getIfAvailable(), matcher);
	}

	@Bean
	public MessageQueueProvider messageQueueProvider(RocketMQProducerProperties rocketMQProducerProperties,
													 RocketMQTemplate rocketMQTemplate) {
		log.debug(AUTOWIRED_ROCKET_MQ_PROVIDER);
		RocketMQConfig config = RocketMQConvertor.INSTANCE.toConfig(rocketMQProperties);
		RocketMQConvertor.INSTANCE.updateConfigFromProducer(rocketMQProducerProperties, config.getProducer());
		return new RocketMQProvider(config, rocketMQTemplate);
	}
}
