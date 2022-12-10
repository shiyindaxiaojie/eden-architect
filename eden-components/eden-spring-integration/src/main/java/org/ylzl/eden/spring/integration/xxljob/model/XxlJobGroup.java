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

package org.ylzl.eden.spring.integration.xxljob.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * XxlJob 执行器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class XxlJobGroup {

	private int id;

	private String appName;

	private String title;

	private int order;

	private int addressType;

	private String addressList;

	private List<String> registryList;

	public List<String> getRegistryList() {
		if (addressList != null && addressList.trim().length() > 0) {
			registryList = new ArrayList<>(Arrays.asList(addressList.split(",")));
		}
		return registryList;
	}
}
