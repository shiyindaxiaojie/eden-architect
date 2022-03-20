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

package org.ylzl.eden.spring.boot.messagequeue.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.ylzl.eden.spring.boot.messagequeue.env.MessageQueueProperties;
import org.ylzl.eden.spring.framework.core.constant.GlobalConstants;
import org.ylzl.eden.spring.integration.messagequeue.common.MessageQueueType;
import org.ylzl.eden.spring.integration.messagequeue.consumer.MessageListener;
import org.ylzl.eden.spring.integration.messagequeue.consumer.MessageQueueConsumer;
import org.ylzl.eden.spring.integration.messagequeue.kafka.KafkaConsumer;
import org.ylzl.eden.spring.integration.messagequeue.kafka.KafkaProvider;
import org.ylzl.eden.spring.integration.messagequeue.producer.MessageQueueProvider;
import org.ylzl.eden.spring.integration.messagequeue.rocketmq.RocketMQConsumer;
import org.ylzl.eden.spring.integration.messagequeue.rocketmq.RocketMQProvider;

import java.util.List;

/**
 * 消息队列自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EnableConfigurationProperties(MessageQueueProperties.class)
@Slf4j
@Configuration
public class MessageQueueAutoConfiguration {

	@ConditionalOnProperty(value = GlobalConstants.PROP_EDEN_PREFIX + ".message-queue.type", havingValue = MessageQueueType.ROCKETMQ)
	@ConditionalOnBean({
		RocketMQProperties.class,
		RocketMQTemplate.class
	})
	@Configuration
	public static class RocketMQMessageQueueAutoConfiguration {

		@Bean
		public MessageQueueConsumer rocketMQConsumer(RocketMQProperties rocketMQProperties,
													 ObjectProvider<List<MessageListener>> messageListeners) {
			return new RocketMQConsumer(rocketMQProperties, messageListeners.getIfAvailable());
		}

		@Bean
		public MessageQueueProvider rocketMQProvider(RocketMQTemplate rocketMQTemplate) {
			return new RocketMQProvider(rocketMQTemplate);
		}
	}

	@ConditionalOnProperty(value = GlobalConstants.PROP_EDEN_PREFIX + ".message-queue.type", havingValue =
		MessageQueueType.KAFKA)
	@ConditionalOnBean({
		KafkaTemplate.class
	})
	@Configuration
	public static class KafkaMessageQueueAutoConfiguration {

		@Bean
		public MessageQueueConsumer kafkaConsumer(KafkaProperties kafkaProperties,
												  ObjectProvider<List<MessageListener>> messageListeners,
												  ObjectProvider<ConsumerFactory<String, String>> consumerFactory,
												  TaskExecutor taskExecutor) {
			return new KafkaConsumer(kafkaProperties, messageListeners.getIfAvailable(),
				consumerFactory.getIfAvailable(), taskExecutor);
		}

		@Bean
		public MessageQueueProvider kafkaProvider(KafkaTemplate<String, String> kafkaTemplate) {
			return new KafkaProvider(kafkaTemplate);
		}
	}
}
