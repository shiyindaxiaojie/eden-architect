package org.ylzl.eden.common.mq.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 消息队列生产者异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class MessageQueueProducerException extends BaseException {

	public MessageQueueProducerException(String errMessage) {
		super("MQ-PRODUCE-500", errMessage);
	}
}
