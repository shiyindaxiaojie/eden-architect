/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.dynamic.sms;

import org.ylzl.eden.dynamic.sms.model.batch.BatchSendSmsRequest;
import org.ylzl.eden.dynamic.sms.model.batch.BatchSendSmsResponse;
import org.ylzl.eden.dynamic.sms.model.multi.MultiSendSmsRequest;
import org.ylzl.eden.dynamic.sms.model.multi.MultiSendSmsResponse;
import org.ylzl.eden.dynamic.sms.model.single.SingleSendSmsRequest;
import org.ylzl.eden.dynamic.sms.model.single.SingleSendSmsResponse;
import org.ylzl.eden.dynamic.sms.model.template.SendTemplateSmsRequest;
import org.ylzl.eden.dynamic.sms.model.template.SendTemplateSmsResponse;

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
