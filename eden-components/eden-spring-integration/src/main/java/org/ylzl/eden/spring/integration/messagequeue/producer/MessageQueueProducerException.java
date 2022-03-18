package org.ylzl.eden.spring.integration.messagequeue.producer;

import org.springframework.http.HttpStatus;
import org.ylzl.eden.spring.framework.cola.exception.BaseException;
import org.ylzl.eden.spring.framework.cola.exception.ThirdServiceErrorType;

/**
 * 消息队列生产者异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class MessageQueueProducerException extends BaseException {

	public MessageQueueProducerException(String errMessage) {
		super(ThirdServiceErrorType.C0121.getErrCode(), errMessage, HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
