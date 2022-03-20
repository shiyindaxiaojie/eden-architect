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

package org.ylzl.eden.spring.cloud.zookeeper.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.zookeeper.ZookeeperProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.integration.zookeeper.config.ZookeeperConfig;
import org.ylzl.eden.spring.integration.zookeeper.core.ZookeeperTemplate;

/**
 * ZooKeeper 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnProperty(name = ZookeeperProperties.PREFIX + ".enabled", havingValue = "true")
@ConditionalOnClass(ZooKeeper.class)
@Slf4j
@Configuration
public class ZookeeperAutoConfiguration {

	private final ZookeeperProperties zookeeperProperties;

	public ZookeeperAutoConfiguration(ZookeeperProperties zookeeperProperties) {
		this.zookeeperProperties = zookeeperProperties;
	}

	@ConditionalOnMissingBean
	@Bean
	public ZookeeperTemplate zooKeeperTemplate() {
		return new ZookeeperTemplate(ZookeeperConfig.builder()
			.connectString(zookeeperProperties.getConnectString())
			.sessionTimeout(zookeeperProperties.getSessionTimeout())
			.build());
	}
}
