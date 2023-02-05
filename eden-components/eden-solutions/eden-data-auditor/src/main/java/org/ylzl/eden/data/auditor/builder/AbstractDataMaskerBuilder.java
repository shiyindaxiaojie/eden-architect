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

package org.ylzl.eden.data.auditor.builder;

import org.ylzl.eden.data.auditor.DataMasker;
import org.ylzl.eden.data.auditor.config.DataMaskerConfig;
import org.ylzl.eden.extension.ExtensionLoader;

/**
 * 数据脱敏构建器抽象
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class AbstractDataMaskerBuilder implements DataMaskerBuilder {

	private DataMaskerConfig config = new DataMaskerConfig();

	private String strategy;

	/**
	 * 设置数据脱敏配置
	 *
	 * @param config 数据脱敏配置
	 * @return DataMaskerBuilder
	 */
	@Override
	public DataMaskerBuilder config(DataMaskerConfig config) {
		this.config = config;
		return this;
	}

	/**
	 * 设置数据脱敏策略
	 *
	 * @param strategy 数据脱敏策略
	 * @return DataMaskerBuilder
	 */
	@Override
	public DataMaskerBuilder strategy(String strategy) {
		this.strategy = strategy;
		return this;
	}

	/**
	 * 获取数据脱敏配置
	 *
	 * @return 数据脱敏配置
	 */
	protected DataMaskerConfig getConfig() {
		return config;
	}

	/**
	 * 获取数据脱敏实例
	 *
	 * @return 数据脱敏实例
	 */
	protected DataMasker getDataMasker() {
		return ExtensionLoader.getExtensionLoader(DataMasker.class).getExtension(strategy);
	}
}
