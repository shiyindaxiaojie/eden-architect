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

package org.ylzl.eden.curator.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.zookeeper.ZookeeperProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.curator.spring.boot.env.CuratorProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * Curator 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = ZookeeperProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE
)
@ConditionalOnClass(CuratorFramework.class)
@EnableConfigurationProperties(CuratorProperties.class)
@RequiredArgsConstructor
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class CuratorAutoConfiguration {

	public static final String AUTOWIRED_CURATOR_FRAMEWORK = "Autowired CuratorFramework";

	private final CuratorProperties curatorProperties;

	private final ZookeeperProperties zookeeperProperties;

	@Bean
	public CuratorFramework curatorFramework() {
		log.debug(AUTOWIRED_CURATOR_FRAMEWORK);
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

		builder.connectString(zookeeperProperties.getConnectString());

		if (curatorProperties.getSessionTimeoutMs() > 0) {
			builder.sessionTimeoutMs(curatorProperties.getSessionTimeoutMs());
		} else if (!zookeeperProperties.getSessionTimeout().isZero()) {
			builder.sessionTimeoutMs((int) zookeeperProperties.getSessionTimeout().toMillis());
		}

		if (curatorProperties.getConnectionTimeoutMs() > 0) {
			builder.connectionTimeoutMs(curatorProperties.getConnectionTimeoutMs());
		} else if (!zookeeperProperties.getConnectionTimeout().isZero()) {
			builder.sessionTimeoutMs((int) zookeeperProperties.getConnectionTimeout().toMillis());
		}

		if (curatorProperties.getEnsembleProvider() != null) {
			builder.ensembleProvider(curatorProperties.getEnsembleProvider());
		}

		if (curatorProperties.getDefaultData() != null) {
			builder.defaultData(curatorProperties.getDefaultData());
		}

		if (curatorProperties.getNamespace() != null) {
			builder.namespace(curatorProperties.getNamespace());
		}

		if (curatorProperties.getMaxCloseWaitMs() > 0) {
			builder.maxCloseWaitMs(curatorProperties.getMaxCloseWaitMs());
		}

		if (curatorProperties.getRetryPolicy() != null) {
			builder.retryPolicy(curatorProperties.getRetryPolicy());
		}

		if (curatorProperties.getThreadFactory() != null) {
			builder.threadFactory(curatorProperties.getThreadFactory());
		}

		if (curatorProperties.getCompressionProvider() != null) {
			builder.compressionProvider(curatorProperties.getCompressionProvider());
		}

		if (curatorProperties.getZookeeperFactory() != null) {
			builder.zookeeperFactory(curatorProperties.getZookeeperFactory());
		}

		if (curatorProperties.getAclProvider() != null) {
			builder.aclProvider(curatorProperties.getAclProvider());
		}

		builder.canBeReadOnly(curatorProperties.isCanBeReadOnly());

		if (!builder.useContainerParentsIfAvailable()) {
			builder.dontUseContainerParents();
		}

		if (curatorProperties.getConnectionStateErrorPolicy() != null) {
			builder.connectionStateErrorPolicy(curatorProperties.getConnectionStateErrorPolicy());
		}

		builder.zk34CompatibilityMode(curatorProperties.isZk34CompatibilityMode());

		if (curatorProperties.getWaitForShutdownTimeoutMs() > 0) {
			builder.waitForShutdownTimeoutMs(curatorProperties.getWaitForShutdownTimeoutMs());
		}

		if (curatorProperties.getConnectionHandlingPolicy() != null) {
			builder.connectionHandlingPolicy(curatorProperties.getConnectionHandlingPolicy());
		}

		if (curatorProperties.getSchemaSet() != null) {
			builder.schemaSet(curatorProperties.getSchemaSet());
		}

		if (curatorProperties.getRunSafeService() != null) {
			builder.runSafeService(curatorProperties.getRunSafeService());
		}

		if (curatorProperties.getConnectionStateListenerManagerFactory() != null) {
			builder.connectionStateListenerManagerFactory(curatorProperties.getConnectionStateListenerManagerFactory());
		}

		CuratorFramework curatorFramework = builder.build();
		curatorFramework.start();
		return curatorFramework;
	}
}
