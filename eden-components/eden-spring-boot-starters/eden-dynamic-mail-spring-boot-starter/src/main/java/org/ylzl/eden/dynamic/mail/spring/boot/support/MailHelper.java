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

package org.ylzl.eden.dynamic.mail.spring.boot.support;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.dynamic.mail.MailTemplate;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;

import java.util.Map;

/**
 * 邮件操作模板帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class MailHelper {

	private static final String MAIL_TYPE_NOT_FOUND = "Mail type named '{}' not found";

	private final String primary;

	public MailTemplate getBean() {
		return getBean(primary);
	}

	public MailTemplate getBean(String mailType) {
		Map<String, MailTemplate> mailTemplates = ApplicationContextHelper.getBeansOfType(MailTemplate.class);
		return mailTemplates.values().stream()
			.filter(predicate -> predicate.mailType().equalsIgnoreCase(mailType))
			.findFirst()
			.orElseThrow(() -> new RuntimeException(MAIL_TYPE_NOT_FOUND));
	}
}
