package org.ylzl.eden.common.mq.core;

import org.ylzl.eden.common.mq.core.producer.MessageQueueProducerException;
import org.ylzl.eden.common.mq.core.producer.MessageSendCallback;
import org.ylzl.eden.common.mq.core.producer.MessageSendResult;

/**
 * 消息队列生产者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
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
