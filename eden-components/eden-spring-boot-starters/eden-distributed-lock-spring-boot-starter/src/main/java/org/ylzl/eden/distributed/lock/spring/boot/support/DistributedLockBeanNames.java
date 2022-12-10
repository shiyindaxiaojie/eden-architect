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

package org.ylzl.eden.distributed.lock.spring.boot.support;

import lombok.Getter;

/**
 * 短信注册Bean
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum DistributedLockBeanNames {

	REDISSON(DistributedLockBeanNames.REDISSON_DISTRIBUTED_LOCK),
	CURATOR(DistributedLockBeanNames.CURATOR_DISTRIBUTED_LOCK),
	JEDIS(DistributedLockBeanNames.JEDIS_DISTRIBUTED_LOCK),
	ZOOKEEPER(DistributedLockBeanNames.ZOOKEEPER_DISTRIBUTED_LOCK);

	public static final String REDISSON_DISTRIBUTED_LOCK = "redissonDistributedLock";

	public static final String CURATOR_DISTRIBUTED_LOCK = "curatorDistributedLock";

	public static final String JEDIS_DISTRIBUTED_LOCK = "jedisDistributedLock";

	public static final String ZOOKEEPER_DISTRIBUTED_LOCK = "zookeeperDistributedLock";

	private final String beanName;

	DistributedLockBeanNames(String beanName) {
		this.beanName = beanName;
	}

	public static DistributedLockBeanNames parse(String type) {
		for (DistributedLockBeanNames distributedLockBeanNames : DistributedLockBeanNames.values()) {
			if (distributedLockBeanNames.name().equalsIgnoreCase(type)) {
				return distributedLockBeanNames;
			}
		}
		return null;
	}
}
