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

import org.ylzl.eden.data.auditor.config.DataDifferConfig;

/**
 * 数据比对构建器抽象
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class AbstractDataDifferBuilder implements DataDifferBuilder {

	private DataDifferConfig dataDifferConfig;

	/**
	 * 设置数据比对配置
	 *
	 * @param dataDifferConfig 数据比对配置
	 * @return DataDifferBuilder
	 */
	@Override
	public DataDifferBuilder dataDifferConfig(DataDifferConfig dataDifferConfig) {
		this.dataDifferConfig = dataDifferConfig;
		return this;
	}

	/**
	 * 获取数据比对配置
	 *
	 * @return 数据比对配置
	 */
	public DataDifferConfig getDataDifferConfig() {
		return dataDifferConfig;
	}
}
