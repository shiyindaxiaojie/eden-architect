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

package org.ylzl.eden.spring.boot.jdbc.env;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.boot.bootstrap.env.EnvironmentOutboundParser;

/**
 * Datasource 解析运行环境变量
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class DatasourceEnvironmentOutboundParser implements EnvironmentOutboundParser {

	private static final String TEMPLATE = "Outbound Datasource: \t{}";

	/**
	 * 返回解析内容
	 *
	 * @param env 运行环境
	 * @return 内容
	 */
	@Override
	public String toString(Environment env) {
		if (!env.containsProperty(DatasourceEnvironment.URL)) {
			return Strings.EMPTY;
		}
		String url = env.getProperty(DatasourceEnvironment.URL);
		if (StringUtils.isNotBlank(url) && url.contains(Strings.PLACEHOLDER)) {
			url = url.substring(0, url.indexOf(Strings.PLACEHOLDER));
		}
		return MessageFormatUtils.format(TEMPLATE, url);
	}
}
