package org.ylzl.eden.dynamic.mq.spring.boot.support;

import lombok.Getter;

/**
 * 消息队列注册类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum MessageQueueBeanType {

	KAFKA(MessageQueueBeanType.KAFKA_CONSUMER, MessageQueueBeanType.KAFKA_PROVIDER),
	ROCKETMQ(MessageQueueBeanType.ROCKETMQ_CONSUMER, MessageQueueBeanType.ROCKETMQ_PROVIDER),
	PULSAR(MessageQueueBeanType.PULSAR_CONSUMER, MessageQueueBeanType.PULSAR_PROVIDER);

	public static final String KAFKA_CONSUMER = "kafkaConsumer";

	public static final String KAFKA_PROVIDER = "kafkaProvider";

	public static final String ROCKETMQ_CONSUMER = "rocketMQConsumer";

	public static final String ROCKETMQ_PROVIDER = "rocketMQProvider";

	public static final String PULSAR_CONSUMER = "pulsarConsumer";

	public static final String PULSAR_PROVIDER = "pulsarProvider";

	private final String consumerName;

	private final String providerName;

	MessageQueueBeanType(String consumerName, String providerName) {
		this.consumerName = consumerName;
		this.providerName = providerName;
	}

	public static MessageQueueBeanType parse(String type) {
		for (MessageQueueBeanType messageQueueBeanType : MessageQueueBeanType.values()) {
			if (messageQueueBeanType.name().equalsIgnoreCase(type)) {
				return messageQueueBeanType;
			}
		}
		return null;
	}
}
