package org.ylzl.eden.spring.integration.messagequeue;

/**
 * 消息队列类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public enum MessageQueueType {

	KAFKA,
	ROCKETMQ,
	PULSAR,
	RABBITMQ;

	@Override
	public String toString() {
		return this.name();
	}
}
