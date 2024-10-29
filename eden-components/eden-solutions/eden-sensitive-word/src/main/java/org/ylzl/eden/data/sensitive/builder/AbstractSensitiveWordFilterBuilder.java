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

package org.ylzl.eden.data.sensitive.builder;

import org.ylzl.eden.data.sensitive.config.SensitiveWordConfig;
import org.ylzl.eden.data.sensitive.SensitiveWordLoader;

/**
 * 敏感词过滤构建器抽象
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class AbstractSensitiveWordFilterBuilder implements SensitiveWordFilterBuilder {

	private SensitiveWordConfig config = new SensitiveWordConfig();

	private SensitiveWordLoader sensitiveWordLoader;

	/**
	 * 设置敏感词过滤配置
	 *
	 * @param config 敏感词过滤配置
	 * @return DataSensitiveFilterBuilder
	 */
	@Override
	public SensitiveWordFilterBuilder config(SensitiveWordConfig config) {
		this.config = config;
		return this;
	}

	/**
	 * 设置敏感词加载器
	 *
	 * @param sensitiveWordLoader 敏感词加载器
	 * @return DataSensitiveFilterBuilder
	 */
	@Override
	public SensitiveWordFilterBuilder sensitiveWordLoader(SensitiveWordLoader sensitiveWordLoader) {
		this.sensitiveWordLoader = sensitiveWordLoader;
		return this;
	}

	/**
	 * 获取敏感词过滤配置
	 *
	 * @return 敏感词配置
	 */
	protected SensitiveWordConfig getConfig() {
		return config;
	}

	/**
	 * 获取敏感词加载器
	 *
	 * @return 敏感词加载器
	 */
	protected SensitiveWordLoader getSensitiveWordLoader() {
		return sensitiveWordLoader;
	}
}
