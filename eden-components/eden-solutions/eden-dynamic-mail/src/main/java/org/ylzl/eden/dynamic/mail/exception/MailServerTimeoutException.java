package org.ylzl.eden.dynamic.mail.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 调用邮件服务器超时
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class MailServerTimeoutException extends BaseException {

	public MailServerTimeoutException(String errMessage) {
		super("MAIL-SERVER-502", errMessage);
	}
}
