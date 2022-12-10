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

package org.ylzl.eden.dynamic.sms.model.template;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 发送模板短信请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class SendTemplateSmsRequest implements Serializable {

	/**
	 * 短信签名
	 */
	private String signName;

	/**
	 * 手机号码
	 */
	private List<String> phoneNumbers;

	/**
	 * 短信模板编号
	 */
	private String templateCode;

	/**
	 * 短信模板参数
	 */
	private Map<String, String> templateParam;
}
