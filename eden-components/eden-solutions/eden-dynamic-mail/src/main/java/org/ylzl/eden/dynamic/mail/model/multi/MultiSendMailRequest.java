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

package org.ylzl.eden.dynamic.mail.model.multi;

import com.google.common.collect.Lists;
import lombok.*;
import org.ylzl.eden.dynamic.mail.model.Mail;

import java.io.Serializable;
import java.util.List;

/**
 * 批量发送个性化邮件请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class MultiSendMailRequest implements Serializable {

	/**
	 * 个性化群发短信
	 */
	private List<Mail> mailList = Lists.newArrayList();

	/**
	 * 添加群发对象
	 *
	 * @param mail
	 */
	public void addSimpleMail(Mail mail) {
		mailList.add(mail);
	}
}
