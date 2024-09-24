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

import org.ylzl.eden.data.filter.MaskingFilter;
import org.ylzl.eden.data.filter.builder.MaskingFilterBuilder;
import org.ylzl.eden.data.filter.config.MaskingConfig;
import org.ylzl.eden.extension.ExtensionLoader;

/**
 * 数据脱敏过滤器帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class MaskingFilterHelper {

	/**
	 * 获取数据脱敏过滤器实例
	 *
	 * @return 数据脱敏过滤器实例
	 */
	public static MaskingFilter maskingFilter() {
		return ExtensionLoader.getExtensionLoader(MaskingFilterBuilder.class).getDefaultExtension().build();
	}

	/**
	 * 获取数据脱敏过滤器实例
	 *
	 * @return 数据脱敏过滤器实例
	 */
	public static MaskingFilter maskingFilter(String spi) {
		return ExtensionLoader.getExtensionLoader(MaskingFilterBuilder.class).getExtension(spi).build();
	}

	/**
	 * 获取数据脱敏过滤器实例
	 *
	 * @param config 配置信息
	 * @return 数据脱敏过滤器实例
	 */
	public static MaskingFilter maskingFilter(MaskingConfig config) {
		return ExtensionLoader.getExtensionLoader(MaskingFilterBuilder.class)
			.getExtension(config.getType())
			.config(config)
			.build();
	}
}
