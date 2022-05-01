package org.ylzl.eden.spring.integration.messagequeue.core.consumer;


import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 消息队列消费者异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class MessageQueueConsumerException extends BaseException {

	public MessageQueueConsumerException(String errMessage) {
		super("C0122", errMessage);
	}
}
