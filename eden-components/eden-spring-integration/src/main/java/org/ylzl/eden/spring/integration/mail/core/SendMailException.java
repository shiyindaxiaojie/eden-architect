package org.ylzl.eden.spring.integration.mail.core;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 发送短信异常
 *
 * @author <a href="mailto:guoyuanlu@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class SendMailException extends BaseException {

	public SendMailException(String errMessage) {
		super("B0001", errMessage);
	}
}
