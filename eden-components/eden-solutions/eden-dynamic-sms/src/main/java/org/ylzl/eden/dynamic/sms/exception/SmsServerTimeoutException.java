package org.ylzl.eden.dynamic.sms.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 调用短信服务器超时
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SmsServerTimeoutException extends BaseException {

	public SmsServerTimeoutException(String errMessage) {
		super("SMS-SERVER-502", errMessage);
	}
}
