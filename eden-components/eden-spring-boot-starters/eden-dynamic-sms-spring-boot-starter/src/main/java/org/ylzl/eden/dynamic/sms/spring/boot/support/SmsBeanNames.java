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

package org.ylzl.eden.dynamic.sms.spring.boot.support;

import lombok.Getter;

/**
 * 短信注册类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum SmsBeanNames {

	ALIYUN(SmsBeanNames.ALIYUN_SMS_TEMPLATE),
	QCLOUD(SmsBeanNames.QCLOUD_SMS_TEMPLATE),
	EMAY(SmsBeanNames.EMAY_SMS_TEMPLATE),
	MONTNETS(SmsBeanNames.MONTNETS_SMS_TEMPLATE);

	public static final String ALIYUN_SMS_TEMPLATE = "aliyunSmsTemplate";

	public static final String QCLOUD_SMS_TEMPLATE = "qcloudSmsTemplate";

	public static final String EMAY_SMS_TEMPLATE = "emaySmsTemplate";

	public static final String MONTNETS_SMS_TEMPLATE = "montnetsSmsTemplate";

	private final String beanName;

	SmsBeanNames(String beanName) {
		this.beanName = beanName;
	}

	public static SmsBeanNames parse(String type) {
		for (SmsBeanNames smsBeanNames : SmsBeanNames.values()) {
			if (smsBeanNames.name().equalsIgnoreCase(type)) {
				return smsBeanNames;
			}
		}
		return null;
	}
}
