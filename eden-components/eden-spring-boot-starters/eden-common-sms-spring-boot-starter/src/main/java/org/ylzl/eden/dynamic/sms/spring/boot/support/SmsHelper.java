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

package org.ylzl.eden.common.sms.spring.boot.support;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ylzl.eden.common.sms.SmsTemplate;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;

import java.util.Map;

/**
 * 短信操作帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class SmsHelper {

	private static final String SMS_TYPE_NOT_FOUND = "Sms type named '{}' not found";

	private final String primary;

	public SmsTemplate getBean() {
		return getBean(primary);
	}

	public SmsTemplate getBean(@NonNull String smsType) {
		Map<String, SmsTemplate> smsTemplates = ApplicationContextHelper.getBeansOfType(SmsTemplate.class);
		return smsTemplates.values().stream()
			.filter(predicate -> predicate.smsType().equalsIgnoreCase(smsType))
			.findFirst()
			.orElseThrow(() -> new RuntimeException(SMS_TYPE_NOT_FOUND));
	}
}
