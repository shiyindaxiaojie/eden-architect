package org.ylzl.eden.dynamic.mail.exception;

import org.ylzl.eden.spring.framework.error.BaseException;

/**
 * 邮件服务器出错
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class MailServerException extends BaseException {

	public MailServerException(String errMessage) {
		super("MAIL-SERVER-500", errMessage);
	}
}
