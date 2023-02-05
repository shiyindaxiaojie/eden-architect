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

package org.ylzl.eden.distributed.uid.builder;

import org.ylzl.eden.distributed.uid.SegmentGenerator;
import org.ylzl.eden.distributed.uid.config.SegmentGeneratorConfig;
import org.ylzl.eden.extension.SPI;

import javax.sql.DataSource;

/**
 * 号段生成器构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI("leaf")
public interface SegmentGeneratorBuilder {

	/**
	 * 设置号段生成器配置
	 *
	 * @param config 号段生成器配置
	 * @return this
	 */
	SegmentGeneratorBuilder config(SegmentGeneratorConfig config);

	/**
	 * 设置数据源
	 *
	 * @param dataSource 数据源
	 * @return this
	 */
	SegmentGeneratorBuilder dataSource(DataSource dataSource);

	/**
	 * 构建号段生成器
	 * @return 号段生成器
	 */
	SegmentGenerator build();
}
