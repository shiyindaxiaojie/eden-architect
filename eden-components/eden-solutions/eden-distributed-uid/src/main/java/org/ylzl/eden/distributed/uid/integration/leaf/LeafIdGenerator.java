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

package org.ylzl.eden.distributed.uid.integration.leaf;

import org.ylzl.eden.distributed.uid.IdGenerator;
import org.ylzl.eden.distributed.uid.IdGeneratorType;
import org.ylzl.eden.distributed.uid.config.IdGeneratorConfig;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.SnowflakeGenerator;
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model.App;

/**
 * Leaf ID生成器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class LeafIdGenerator implements IdGenerator {

	private final SnowflakeGenerator snowflakeGenerator;

	public LeafIdGenerator(IdGeneratorConfig config, App app) {
		this.snowflakeGenerator = new SnowflakeGenerator(config, app);
	}

	/**
	 * 生成器类型
	 *
	 * @return 生成器类型
	 */
	@Override
	public String generatorType() {
		return IdGeneratorType.LEAF.name();
	}

	/**
	 * 获取ID
	 *
	 * @return ID
	 */
	@Override
	public long nextId() {
		return snowflakeGenerator.nextId();
	}
}
