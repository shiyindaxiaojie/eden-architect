package org.ylzl.eden.spring.integration.messagequeue.producer;

/**
 * 消息队列生产者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface MessageQueueProvider {

	void send(Message message) throws MessageQueueProducerException;
}
