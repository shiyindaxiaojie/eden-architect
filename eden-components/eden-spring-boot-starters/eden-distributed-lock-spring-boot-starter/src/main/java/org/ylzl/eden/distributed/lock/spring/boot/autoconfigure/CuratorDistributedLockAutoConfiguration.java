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

package org.ylzl.eden.distributed.lock.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.distributed.lock.DistributedLock;
import org.ylzl.eden.distributed.lock.integration.curator.CuratorDistributedLock;
import org.ylzl.eden.distributed.lock.spring.boot.env.DistributedLockProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * Curator 分布式锁自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = DistributedLockProperties.Curator.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE
)
@ConditionalOnClass(CuratorFramework.class)
@AutoConfigureBefore(DistributedLockAutoConfiguration.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class CuratorDistributedLockAutoConfiguration {

	public static final String AUTOWIRED_CURATOR_DISTRIBUTED_LOCK = "Autowired CuratorDistributedLock";

	@Bean
	public DistributedLock distributedLock(CuratorFramework curatorFramework) {
		log.debug(AUTOWIRED_CURATOR_DISTRIBUTED_LOCK);
		return new CuratorDistributedLock(curatorFramework);
	}
}
