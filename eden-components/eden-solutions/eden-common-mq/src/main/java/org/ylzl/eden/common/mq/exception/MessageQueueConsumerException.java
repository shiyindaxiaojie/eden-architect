package org.ylzl.eden.common.mq.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 消息队列消费者异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class MessageQueueConsumerException extends BaseException {

	public MessageQueueConsumerException(String errMessage) {
		super("MQ-CONSUME-500", errMessage);
	}
}
