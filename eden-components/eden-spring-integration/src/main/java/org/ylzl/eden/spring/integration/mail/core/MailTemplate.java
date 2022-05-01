package org.ylzl.eden.spring.integration.mail.core;

/**
 * 邮件操作模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface MailTemplate {

	/**
	 * 单条发送邮件
	 *
	 * @param request 发送邮件请求
	 * @return 发送邮件响应
	 */
	SingleSendMailResponse singleSend(SingleSendMailRequest request);

	/**
	 * 批量发送个性化邮件
	 *
	 * @param request 发送邮件请求
	 * @return 发送邮件响应
	 */
	MultiSendMailResponse multiSend(MultiSendMailRequest request);
}
