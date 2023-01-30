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

import org.ylzl.eden.distributed.uid.config.SegmentGeneratorConfig;

import javax.sql.DataSource;

/**
 * 号段生成器构建抽象
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class AbstractSegmentGeneratorBuilder implements SegmentGeneratorBuilder {

	private SegmentGeneratorConfig config = new SegmentGeneratorConfig();

	private DataSource dataSource;

	/**
	 * 设置号段生成器配置
	 *
	 * @param config 雪花算法生成器配置
	 * @return this
	 */
	@Override
	public SegmentGeneratorBuilder config(SegmentGeneratorConfig config) {
		this.config = config;
		return this;
	}

	/**
	 * 设置数据源
	 *
	 * @param dataSource 数据源
	 * @return this
	 */
	@Override
	public SegmentGeneratorBuilder dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

	/**
	 * 获取号段生成器配置
	 *
	 * @return 号段生成器配置
	 */
	protected SegmentGeneratorConfig getConfig() {
		return config;
	}

	/**
	 * 获取数据源
	 *
	 * @return 数据源
	 */
	public DataSource getDataSource() {
		return dataSource;
	}
}
