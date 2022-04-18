package org.ylzl.eden.spring.integration.messagequeue.common;

import lombok.experimental.UtilityClass;

/**
 * 消息队列类型（内部）
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class MessageQueueType {

	public static final String KAFKA = "Kafka";

	public static final String ROCKETMQ = "RocketMQ";

	public static final String PULSAR = "Pulsar";

	public static final String RABBITMQ = "RabbitMQ";
}
