package org.ylzl.eden.mail.adapter.core;

import org.ylzl.eden.mail.adapter.core.multi.MultiSendMailRequest;
import org.ylzl.eden.mail.adapter.core.multi.MultiSendMailResponse;
import org.ylzl.eden.mail.adapter.core.single.SingleSendMailRequestModel;
import org.ylzl.eden.mail.adapter.core.single.SingleSendMailResponse;

/**
 * 邮件操作模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface MailTemplate {

	/**
	 * 单条发送邮件
	 *
	 * @param request 发送邮件请求
	 * @return 发送邮件响应
	 */
	SingleSendMailResponse singleSend(SingleSendMailRequestModel request);

	/**
	 * 批量发送个性化邮件
	 *
	 * @param request 发送邮件请求
	 * @return 发送邮件响应
	 */
	MultiSendMailResponse multiSend(MultiSendMailRequest request);
}
