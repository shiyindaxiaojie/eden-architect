package org.ylzl.eden.sms.adapter.core;

import org.ylzl.eden.sms.adapter.core.batch.BatchSendSmsRequest;
import org.ylzl.eden.sms.adapter.core.batch.BatchSendSmsResponse;
import org.ylzl.eden.sms.adapter.core.multi.MultiSendSmsRequest;
import org.ylzl.eden.sms.adapter.core.multi.MultiSendSmsResponse;
import org.ylzl.eden.sms.adapter.core.single.SingleSendSmsRequest;
import org.ylzl.eden.sms.adapter.core.single.SingleSendSmsResponse;
import org.ylzl.eden.sms.adapter.core.template.SendTemplateSmsRequest;
import org.ylzl.eden.sms.adapter.core.template.SendTemplateSmsResponse;

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
