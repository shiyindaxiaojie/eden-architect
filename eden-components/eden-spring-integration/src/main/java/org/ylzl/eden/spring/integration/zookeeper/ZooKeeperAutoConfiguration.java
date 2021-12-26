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

package org.ylzl.eden.spring.integration.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.framework.core.constant.SpringFrameworkConstants;
import org.ylzl.eden.spring.integration.zookeeper.core.ZooKeeperTemplate;
import org.ylzl.eden.spring.integration.zookeeper.support.lock.DistributedZooKeeperLock;
import org.ylzl.eden.spring.integration.zookeeper.support.lock.ZooKeeperLock;

/**
 * ZooKeeper 自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnClass(ZooKeeper.class)
@ConditionalOnExpression(ZooKeeperAutoConfiguration.EXP_ZOOKEEPER_ENABLED)
@EnableConfigurationProperties(ZooKeeperProperties.class)
@Slf4j
@Configuration
public class ZooKeeperAutoConfiguration {

  public static final String EXP_ZOOKEEPER_ENABLED =
      "${" + SpringFrameworkConstants.PROP_SPRING_PREFIX + ".zookeeper.enabled:true}";

  private final ZooKeeperProperties zooKeeperProperties;

  public ZooKeeperAutoConfiguration(ZooKeeperProperties zooKeeperProperties) {
    this.zooKeeperProperties = zooKeeperProperties;
  }

  @ConditionalOnMissingBean
  @Bean
  public ZooKeeperTemplate zooKeeperTemplate() {
    return new ZooKeeperTemplate(
        zooKeeperProperties.getConnectString(), zooKeeperProperties.getSessionTimeout());
  }

  @ConditionalOnBean(ZooKeeperTemplate.class)
  @ConditionalOnMissingBean
  @Bean
  public ZooKeeperLock zooKeeperLock(ZooKeeperTemplate zooKeeperTemplate) {
    return new DistributedZooKeeperLock(zooKeeperTemplate);
  }
}
