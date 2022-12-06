package org.ylzl.eden.dynamic.mail.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 发送邮件异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SendMailException extends BaseException {

	public SendMailException(String errMessage) {
		super("MAIL-SEND-500", errMessage);
	}
}
