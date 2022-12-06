package org.ylzl.eden.dynamic.mq.consumer;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 消息消费异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class MessageConsumeException extends BaseException {

	public MessageConsumeException(String errMessage) {
		super("MQ-CONSUME-500", errMessage);
	}
}
