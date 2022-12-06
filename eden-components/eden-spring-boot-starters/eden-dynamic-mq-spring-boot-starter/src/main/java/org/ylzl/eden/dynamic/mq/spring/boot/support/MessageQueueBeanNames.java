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
