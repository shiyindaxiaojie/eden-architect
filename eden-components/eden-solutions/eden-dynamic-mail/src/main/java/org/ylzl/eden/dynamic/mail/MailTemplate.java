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

package org.ylzl.eden.dynamic.mail;

import org.ylzl.eden.dynamic.mail.model.multi.MultiSendMailRequest;
import org.ylzl.eden.dynamic.mail.model.multi.MultiSendMailResponse;
import org.ylzl.eden.dynamic.mail.model.single.SingleSendMailRequest;
import org.ylzl.eden.dynamic.mail.model.single.SingleSendMailResponse;

/**
 * 邮件操作模板
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface MailTemplate {

	/**
	 * 邮件类型
	 *
	 * @return 邮件类型
	 */
	String mailType();

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
