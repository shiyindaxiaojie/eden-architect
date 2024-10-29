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

package org.ylzl.eden.data.sensitive.support;

import org.ylzl.eden.data.sensitive.SensitiveWordFilter;
import org.ylzl.eden.data.sensitive.builder.SensitiveWordFilterBuilder;
import org.ylzl.eden.data.sensitive.SensitiveWordLoader;
import org.ylzl.eden.extension.ExtensionLoader;

/**
 * 敏感词过滤器帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class SensitiveWordFilterHelper {

	/**
	 * 获取敏感词过滤器实例
	 *
	 * @param sensitiveWordLoader 敏感词处理器
	 * @return 敏感词过滤器实例
	 */
	public static SensitiveWordFilter sensitiveFilter(SensitiveWordLoader sensitiveWordLoader) {
		return ExtensionLoader.getExtensionLoader(SensitiveWordFilterBuilder.class).getDefaultExtension()
			.sensitiveWordLoader(sensitiveWordLoader).build();
	}

	/**
	 * 获取敏感词过滤器实例
	 *
	 * @param spi                 扩展点
	 * @param sensitiveWordLoader 敏感词处理器
	 * @return 敏感词过滤器实例
	 */
	public static SensitiveWordFilter sensitiveFilter(String spi, SensitiveWordLoader sensitiveWordLoader) {
		return ExtensionLoader.getExtensionLoader(SensitiveWordFilterBuilder.class).getExtension(spi)
			.sensitiveWordLoader(sensitiveWordLoader).build();
	}
}
