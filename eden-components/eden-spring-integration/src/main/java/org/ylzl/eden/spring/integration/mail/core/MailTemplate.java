package org.ylzl.eden.spring.integration.mail.core;

/**
 * 邮件操作模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface MailTemplate {

	/**
	 * 发送邮件
	 *
	 * @param sendMailRequest 发送邮件请求
	 * @return 发送邮件响应
	 */
	SendMailResponse send(SendMailRequest sendMailRequest);
}
