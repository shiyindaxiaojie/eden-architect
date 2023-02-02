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

import org.ylzl.eden.distributed.uid.SnowflakeGenerator;
import org.ylzl.eden.distributed.uid.builder.AbstractSnowflakeGeneratorBuilder;
import org.ylzl.eden.distributed.uid.builder.SnowflakeGeneratorBuilder;

/**
 * Leaf雪花算法生成器构建
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class LeafSnowflakeGeneratorBuilder extends AbstractSnowflakeGeneratorBuilder implements SnowflakeGeneratorBuilder {

	/**
	 * 构建雪花算法生成器
	 *
	 * @return 雪花算法生成器
	 */
	@Override
	public SnowflakeGenerator build() {
		return new LeafSnowflakeGenerator(this.getConfig(), this.getApp());
	}
}
