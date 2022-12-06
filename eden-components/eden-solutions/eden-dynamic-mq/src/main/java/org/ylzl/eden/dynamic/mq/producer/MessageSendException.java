package org.ylzl.eden.dynamic.mq.producer;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 消息发送异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class MessageSendException extends BaseException {

	public MessageSendException(String errMessage) {
		super("MQ-PRODUCE-500", errMessage);
	}
}
