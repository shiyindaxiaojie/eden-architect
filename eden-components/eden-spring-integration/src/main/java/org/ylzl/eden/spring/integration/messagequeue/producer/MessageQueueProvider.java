package org.ylzl.eden.spring.integration.messagequeue.producer;

/**
 * 消息队列生产者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface MessageQueueProvider {

	/**
	 * 同步发送消息
	 *
	 * @param message
	 * @return
	 * @throws MessageQueueProducerException
	 */
	MessageSendResult syncSend(Message message) throws MessageQueueProducerException;

	/**
	 * 异步发送消息
	 *
	 * @param message
	 * @param messageCallback
	 * @throws MessageQueueProducerException
	 */
	void asyncSend(Message message, MessageSendCallback messageCallback) throws MessageQueueProducerException;
}
