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

package org.ylzl.eden.dynamic.mq.spring.boot.support;

import lombok.Getter;

/**
 * 消息队列注册类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum MessageQueueBeanNames {

	KAFKA(MessageQueueBeanNames.KAFKA_CONSUMER, MessageQueueBeanNames.KAFKA_PROVIDER),
	ROCKETMQ(MessageQueueBeanNames.ROCKETMQ_CONSUMER, MessageQueueBeanNames.ROCKETMQ_PROVIDER),
	PULSAR(MessageQueueBeanNames.PULSAR_CONSUMER, MessageQueueBeanNames.PULSAR_PROVIDER);

	public static final String KAFKA_CONSUMER = "kafkaConsumer";

	public static final String KAFKA_PROVIDER = "kafkaProvider";

	public static final String ROCKETMQ_CONSUMER = "rocketMQConsumer";

	public static final String ROCKETMQ_PROVIDER = "rocketMQProvider";

	public static final String PULSAR_CONSUMER = "pulsarConsumer";

	public static final String PULSAR_PROVIDER = "pulsarProvider";

	private final String consumerBeanName;

	private final String providerBeanName;

	MessageQueueBeanNames(String consumerBeanName, String providerBeanName) {
		this.consumerBeanName = consumerBeanName;
		this.providerBeanName = providerBeanName;
	}

	public static MessageQueueBeanNames parse(String type) {
		for (MessageQueueBeanNames messageQueueBeanNames : MessageQueueBeanNames.values()) {
			if (messageQueueBeanNames.name().equalsIgnoreCase(type)) {
				return messageQueueBeanNames;
			}
		}
		return null;
	}
}
