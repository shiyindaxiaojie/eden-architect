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
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.dynamic.mq.MessageQueueConsumer;
import org.ylzl.eden.dynamic.mq.MessageQueueProvider;
import org.ylzl.eden.dynamic.mq.integration.kafka.KafkaConsumer;
import org.ylzl.eden.dynamic.mq.integration.kafka.KafkaProvider;
import org.ylzl.eden.dynamic.mq.integration.kafka.config.KafkaConfig;
import org.ylzl.eden.dynamic.mq.spring.boot.env.MessageQueueProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.env.convertor.KafkaConvertor;
import org.ylzl.eden.dynamic.mq.MessageQueueType;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import java.util.List;
import java.util.function.Function;

/**
 * Kafka 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = MessageQueueProperties.Kafka.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE
)
@ConditionalOnExpression("${spring.kafka.enabled:true}")
@ConditionalOnBean(KafkaProperties.class)
@ConditionalOnClass(KafkaTemplate.class)
@AutoConfigureAfter(MessageQueueAutoConfiguration.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class KafkaMessageQueueAutoConfiguration {

	private static final String AUTOWIRED_KAKFA_CONSUMER = "Autowired KafkaConsumer";

	private static final String AUTOWIRED_KAFKA_PROVIDER = "Autowired KafkaProvider";

	private final MessageQueueProperties messageQueueProperties;

	private final KafkaProperties kafkaProperties;

	@Bean
	public KafkaConsumer kafkaConsumer(ObjectProvider<List<MessageQueueConsumer>> messageListeners,
									   ObjectProvider<ConsumerFactory<String, String>> consumerFactory) {
		log.debug(AUTOWIRED_KAKFA_CONSUMER);
		Function<String, Boolean> matcher = type -> StringUtils.isBlank(type) && messageQueueProperties.getPrimary() != null?
			MessageQueueType.KAFKA.name().equalsIgnoreCase(messageQueueProperties.getPrimary()):
			MessageQueueType.KAFKA.name().equalsIgnoreCase(type);
		KafkaConfig kafkaConfig = KafkaConvertor.INSTANCE.toConfig(kafkaProperties);
		return new KafkaConsumer(kafkaConfig, messageListeners.getIfAvailable(),
			consumerFactory.getIfAvailable(), matcher);
	}

	@Bean
	public MessageQueueProvider messageQueueProvider(KafkaTemplate<String, String> kafkaTemplate) {
		log.debug(AUTOWIRED_KAFKA_PROVIDER);
		return new KafkaProvider(kafkaTemplate);
	}
}
