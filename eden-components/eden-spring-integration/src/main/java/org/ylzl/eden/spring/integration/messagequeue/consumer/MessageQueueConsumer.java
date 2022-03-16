package org.ylzl.eden.spring.integration.messagequeue.consumer;

/**
 * 消息队列消费者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface MessageQueueConsumer {

	void consume() throws MessageQueueConsumerException;
}
