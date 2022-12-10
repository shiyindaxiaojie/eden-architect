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

package org.ylzl.eden.distributed.uid.spring.boot.support;

import lombok.Getter;

/**
 * 分布式唯一ID注册类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum DistributedUIDBeanNames {

	LEAF(DistributedUIDBeanNames.LEAF_DISTRIBUTED_UID),
	SNOWFLAKE(DistributedUIDBeanNames.SNOWFLAKE_DISTRIBUTED_UID),
	TINY_ID(DistributedUIDBeanNames.TINY_ID_DISTRIBUTED_UID),
	UID_GENERATOR(DistributedUIDBeanNames.UID_GENERATOR_DISTRIBUTED_UID);

	public static final String LEAF_DISTRIBUTED_UID = "leafDistributedUID";

	public static final String SNOWFLAKE_DISTRIBUTED_UID = "snowflakeDistributedUID";

	public static final String TINY_ID_DISTRIBUTED_UID = "tinyIdDistributedUID";

	public static final String UID_GENERATOR_DISTRIBUTED_UID = "uidGeneratorDistributedUID";

	private final String beanName;

	DistributedUIDBeanNames(String beanName) {
		this.beanName = beanName;
	}

	public static DistributedUIDBeanNames parse(String type) {
		for (DistributedUIDBeanNames distributedUIDBeanNames : DistributedUIDBeanNames.values()) {
			if (distributedUIDBeanNames.name().equalsIgnoreCase(type)) {
				return distributedUIDBeanNames;
			}
		}
		return null;
	}
}
