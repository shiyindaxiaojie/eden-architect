/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.boot.distributelock.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.distributelock.env.DistributedLockProperties;
import org.ylzl.eden.spring.integration.core.constant.SpringIntegrationConstants;
import org.ylzl.eden.spring.integration.distributelock.core.DistributedLockManager;
import org.ylzl.eden.spring.integration.distributelock.curator.CuratorDistributedLockManager;
import org.ylzl.eden.spring.integration.distributelock.redisson.RedissonDistributedLockManager;

/**
 * 分布式锁自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EnableConfigurationProperties(DistributedLockProperties.class)
@Slf4j
@Configuration
public class DistributeLockAutoConfiguration {

	@ConditionalOnProperty(value = SpringIntegrationConstants.PROP_PREFIX + ".distribute-lock", havingValue = "redisson")
	@ConditionalOnClass(Redisson.class)
	@ConditionalOnBean(RedissonClient.class)
	@Bean
	public DistributedLockManager distributedLockManager(RedissonClient redissonClient) {
		return new RedissonDistributedLockManager(redissonClient);
	}

	@ConditionalOnProperty(value = SpringIntegrationConstants.PROP_PREFIX + ".distribute-lock", havingValue = "curator")
	@ConditionalOnClass(CuratorFramework.class)
	@Bean
	public DistributedLockManager distributedLockManager() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
			"", retryPolicy);
		return new CuratorDistributedLockManager(curatorFramework);
	}
}
