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

package org.ylzl.eden.distributed.uid.support;

import org.ylzl.eden.distributed.uid.SegmentGenerator;
import org.ylzl.eden.distributed.uid.builder.SegmentGeneratorBuilder;
import org.ylzl.eden.distributed.uid.config.SegmentGeneratorConfig;
import org.ylzl.eden.extension.ExtensionLoader;

import javax.sql.DataSource;

/**
 * 号段生成器帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class SegmentGeneratorHelper {

	/**
	 * 获取号段生成器实例
	 *
	 * @param dataSource 数据源
	 * @return 号段生成器实例
	 */
	public static SegmentGenerator segmentGenerator(DataSource dataSource) {
		return ExtensionLoader.getExtensionLoader(SegmentGeneratorBuilder.class).getDefaultExtension()
			.dataSource(dataSource)
			.build();
	}

	/**
	 * 获取号段生成器实例
	 *
	 * @param dataSource 数据源
	 * @param config 配置
	 * @return 号段生成器实例
	 */
	public static SegmentGenerator segmentGenerator(DataSource dataSource, SegmentGeneratorConfig config) {
		return ExtensionLoader.getExtensionLoader(SegmentGeneratorBuilder.class).getDefaultExtension()
			.dataSource(dataSource)
			.config(config)
			.build();
	}


	/**
	 * 获取号段生成器实例
	 *
	 * @param spi 扩展点
	 * @param dataSource 数据源
	 * @return 号段生成器实例
	 */
	public static SegmentGenerator segmentGenerator(String spi, DataSource dataSource) {
		return ExtensionLoader.getExtensionLoader(SegmentGeneratorBuilder.class).getExtension(spi)
			.dataSource(dataSource)
			.build();
	}

	/**
	 * 获取号段生成器实例
	 *
	 * @param spi 扩展点
	 * @param dataSource 数据源
	 * @param config 配置
	 * @return 号段生成器实例
	 */
	public static SegmentGenerator segmentGenerator(String spi, DataSource dataSource, SegmentGeneratorConfig config) {
		return ExtensionLoader.getExtensionLoader(SegmentGeneratorBuilder.class).getExtension(spi)
			.dataSource(dataSource)
			.config(config)
			.build();
	}
}
