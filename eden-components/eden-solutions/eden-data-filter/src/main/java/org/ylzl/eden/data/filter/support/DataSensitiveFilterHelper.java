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

package org.ylzl.eden.data.filter.support;

import org.ylzl.eden.data.filter.DataSensitiveFilter;
import org.ylzl.eden.data.filter.builder.DataSensitiveFilterBuilder;
import org.ylzl.eden.data.filter.sensitive.SensitiveWordProcessor;
import org.ylzl.eden.extension.ExtensionLoader;

/**
 * 敏感词过滤器帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class DataSensitiveFilterHelper {

	/**
	 * 获取敏感词过滤器实例
	 *
	 * @param sensitiveWordProcessor 敏感词处理器
	 * @return 数据脱敏实例
	 */
	public static DataSensitiveFilter dataSensitiveFilter(SensitiveWordProcessor sensitiveWordProcessor) {
		return ExtensionLoader.getExtensionLoader(DataSensitiveFilterBuilder.class).getDefaultExtension()
			.sensitiveWordProcessor(sensitiveWordProcessor).build();
	}

	/**
	 * 获取敏感词过滤器实例
	 *
	 * @param spi 扩展点
	 * @param sensitiveWordProcessor 敏感词处理器
	 * @return 数据脱敏实例
	 */
	public static DataSensitiveFilter dataSensitiveFilter(String spi, SensitiveWordProcessor sensitiveWordProcessor) {
		return ExtensionLoader.getExtensionLoader(DataSensitiveFilterBuilder.class).getExtension(spi)
			.sensitiveWordProcessor(sensitiveWordProcessor).build();
	}
}
