package org.ylzl.eden.common.sms.core;

import org.ylzl.eden.common.sms.batch.BatchSendSmsRequest;
import org.ylzl.eden.common.sms.batch.BatchSendSmsResponse;
import org.ylzl.eden.common.sms.multi.MultiSendSmsRequest;
import org.ylzl.eden.common.sms.multi.MultiSendSmsResponse;
import org.ylzl.eden.common.sms.single.SingleSendSmsRequest;
import org.ylzl.eden.common.sms.single.SingleSendSmsResponse;
import org.ylzl.eden.common.sms.template.SendTemplateSmsRequest;
import org.ylzl.eden.common.sms.template.SendTemplateSmsResponse;

/**
 * 短信操作模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface SmsTemplate {

	/**
	 * 单条发送
	 *
	 * @param request 发送短信请求
	 * @return 发送短信响应
	 */
	SingleSendSmsResponse singleSend(SingleSendSmsRequest request);

	/**
	 * 相同内容群发
	 *
	 * @param request 发送短信请求
	 * @return 发送短信响应
	 */
	BatchSendSmsResponse batchSend(BatchSendSmsRequest request);

	/**
	 * 个性化群发
	 *
	 * @param request 发送短信请求
	 * @return 发送短信响应
	 */
	MultiSendSmsResponse multiSend(MultiSendSmsRequest request);

	/**
	 * 模板发送
	 *
	 * @param request 发送短信请求
	 * @return 发送短信响应
	 */
	SendTemplateSmsResponse templateSend(SendTemplateSmsRequest request);
}
