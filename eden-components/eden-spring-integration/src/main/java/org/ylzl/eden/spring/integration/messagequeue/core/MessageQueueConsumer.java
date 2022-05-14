package org.ylzl.eden.spring.integration.messagequeue.core;


import org.ylzl.eden.spring.integration.messagequeue.core.consumer.Acknowledgement;

import java.util.List;

/**
 * 消息队列消费者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface MessageQueueConsumer {

	/**
	 * 消费消息
	 *
	 * @param messages
	 */
	void consume(List<Message> messages, Acknowledgement ack);
}
