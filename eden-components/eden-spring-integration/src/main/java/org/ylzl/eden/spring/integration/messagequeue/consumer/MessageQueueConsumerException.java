package org.ylzl.eden.spring.integration.messagequeue.consumer;

import org.springframework.http.HttpStatus;
import org.ylzl.eden.spring.framework.cola.exception.BaseException;
import org.ylzl.eden.spring.framework.cola.exception.ThirdServiceErrorType;

/**
 * 消息队列消费者异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class MessageQueueConsumerException extends BaseException {

	public MessageQueueConsumerException(String errMessage) {
		super(ThirdServiceErrorType.C0122.getErrCode(), errMessage, HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
