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

package org.ylzl.eden.distributed.uid.integration.leaf.snowflake.coordinator;

import org.ylzl.eden.distributed.uid.config.IdGeneratorConfig;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model.App;
import org.ylzl.eden.extension.ExtensionLoader;

/**
 * 雪花算法协调器构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class SnowflakeCoordinatorBuilder {

	/**
	 * 构建雪花算法协调器
	 *
	 * @param config ID生成器配置
	 * @param app 应用信息
	 * @return 雪花算法协调器实例
	 */
	public static SnowflakeCoordinator build(IdGeneratorConfig config, App app) {
		return ExtensionLoader.getExtensionLoader(SnowflakeCoordinator.class)
			.getExtension(config.getCoordinator().getType())
			.config(config)
			.app(app);
	}
}
