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

package org.ylzl.eden.extension.common;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义资源路径
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see java.net.URL
 * @see java.net.URI
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class URL implements Serializable {

	private final Map<String, String> parameters;

	public String getParameter(String key) {
		return parameters.get(key);
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
}
