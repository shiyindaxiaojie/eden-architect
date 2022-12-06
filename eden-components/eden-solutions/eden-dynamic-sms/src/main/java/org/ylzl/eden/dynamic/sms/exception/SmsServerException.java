package org.ylzl.eden.dynamic.sms.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 短信服务器出错
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SmsServerException extends BaseException {

	public SmsServerException(String errMessage) {
		super("SMS-SERVER-500", errMessage);
	}
}
