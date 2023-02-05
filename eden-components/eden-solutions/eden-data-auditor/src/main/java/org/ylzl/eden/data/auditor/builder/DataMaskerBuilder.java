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
import org.ylzl.eden.extension.SPI;

/**
 * 数据脱敏构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI("jackson")
public interface DataMaskerBuilder {

	/**
	 * 设置数据脱敏配置
	 *
	 * @param config 数据脱敏配置
	 * @return DataMaskerBuilder
	 */
	DataMaskerBuilder config(DataMaskerConfig config);

	/**
	 * 设置数据脱敏策略
	 *
	 * @param strategy 数据脱敏策略
	 * @return DataMaskerBuilder
	 */
	DataMaskerBuilder strategy(String strategy);

	/**
	 * 构建数据脱敏实例
	 *
	 * @return 数据脱敏实例
	 */
	DataMasker build();
}
