package org.ylzl.eden.dynamic.mq;

import org.ylzl.eden.dynamic.mq.model.Message;
import org.ylzl.eden.dynamic.mq.producer.MessageSendCallback;
import org.ylzl.eden.dynamic.mq.producer.MessageSendResult;

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
	 */
	MessageSendResult syncSend(Message message);

	/**
	 * 异步发送消息
	 *
	 * @param message
	 * @param messageCallback
	 */
	void asyncSend(Message message, MessageSendCallback messageCallback);
}
