package org.ylzl.eden.common.mq.core;

import lombok.Getter;

/**
 * 消息队列类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum MessageQueueType {

	DEFAULT(null, null),
	KAFKA(MessageQueueType.KAFKA_CONSUMER, MessageQueueType.KAFKA_PROVIDER),
	ROCKETMQ(MessageQueueType.ROCKETMQ_CONSUMER, MessageQueueType.ROCKETMQ_PROVIDER),
	PULSAR(MessageQueueType.PULSAR_CONSUMER, MessageQueueType.PULSAR_PROVIDER);

	public static final String KAFKA_CONSUMER = "kafkaConsumer";

	public static final String KAFKA_PROVIDER = "kafkaProvider";

	public static final String ROCKETMQ_CONSUMER = "rocketMQConsumer";

	public static final String ROCKETMQ_PROVIDER = "rocketMQProvider";

	public static final String PULSAR_CONSUMER = "pulsarConsumer";

	public static final String PULSAR_PROVIDER = "pulsarProvider";

	private final String consumerName;

	private final String providerName;

	MessageQueueType(String consumerName, String providerName) {
		this.consumerName = consumerName;
		this.providerName = providerName;
	}

	public static MessageQueueType parse(String type) {
		for (MessageQueueType messageQueueType : MessageQueueType.values()) {
			if (messageQueueType.name().equalsIgnoreCase(type)) {
				return messageQueueType;
			}
		}
		return null;
	}
}
