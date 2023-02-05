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

import org.ylzl.eden.distributed.uid.config.SnowflakeGeneratorConfig;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model.App;

/**
 * 雪花算法生成器构建抽象
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class AbstractSnowflakeGeneratorBuilder implements SnowflakeGeneratorBuilder {

	private SnowflakeGeneratorConfig config = new SnowflakeGeneratorConfig();

	private App app = new App();

	/**
	 * 设置雪花算法生成器配置
	 *
	 * @param config 雪花算法生成器配置
	 * @return this
	 */
	@Override
	public SnowflakeGeneratorBuilder config(SnowflakeGeneratorConfig config) {
		this.config = config;
		return this;
	}

	/**
	 * 设置应用信息
	 *
	 * @param app 应用信息
	 * @return this
	 */
	@Override
	public SnowflakeGeneratorBuilder app(App app) {
		this.app = app;
		return this;
	}

	/**
	 * 获取雪花算法生成器配置
	 *
	 * @return 雪花算法生成器配置
	 */
	protected SnowflakeGeneratorConfig getConfig() {
		return config;
	}

	/**
	 * 获取应用信息
	 *
	 * @return 应用信息
	 */
	protected App getApp() {
		return app;
	}
}
