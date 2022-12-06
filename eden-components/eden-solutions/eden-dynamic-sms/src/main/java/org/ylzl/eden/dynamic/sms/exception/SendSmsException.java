package org.ylzl.eden.dynamic.sms.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 发送短信异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SendSmsException extends BaseException {

	public SendSmsException(String errMessage) {
		super("SMS-SEND-500", errMessage);
	}
}
