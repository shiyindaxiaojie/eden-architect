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

import org.ylzl.eden.distributed.uid.SnowflakeGenerator;
import org.ylzl.eden.distributed.uid.builder.SnowflakeGeneratorBuilder;
import org.ylzl.eden.distributed.uid.config.SnowflakeGeneratorConfig;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model.App;
import org.ylzl.eden.extension.ExtensionLoader;

/**
 * 雪花算法生成器帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class SnowflakeGeneratorHelper {

	/**
	 * 获取雪花算法生成器实例
	 *
	 * @param app 应用信息
	 * @return 雪花算法生成器实例
	 */
	public static SnowflakeGenerator snowflakeGenerator(App app) {
		return ExtensionLoader.getExtensionLoader(SnowflakeGeneratorBuilder.class).getDefaultExtension()
			.app(app)
			.build();
	}

	/**
	 * 获取雪花算法生成器实例
	 *
	 * @param spi 扩展点
	 * @param app 应用信息
	 * @return 雪花算法生成器实例
	 */
	public static SnowflakeGenerator snowflakeGenerator(String spi, App app) {
		return ExtensionLoader.getExtensionLoader(SnowflakeGeneratorBuilder.class).getExtension(spi)
			.app(app)
			.build();
	}

	/**
	 * 获取雪花算法生成器实例
	 *
	 * @param config 配置信息
	 * @param app    应用信息
	 * @return 雪花算法生成器实例
	 */
	public static SnowflakeGenerator snowflakeGenerator(SnowflakeGeneratorConfig config, App app) {
		return ExtensionLoader.getExtensionLoader(SnowflakeGeneratorBuilder.class)
			.getExtension(config.getType())
			.app(app)
			.config(config)
			.build();
	}
}
