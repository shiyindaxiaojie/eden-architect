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

package org.ylzl.eden.spring.boot.cloud.configserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;

/**
 * ConfigServer 自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@ConditionalOnClass({EnableConfigServer.class})
@ConditionalOnExpression(ConfigServerAutoConfiguration.EXPS_CONFIG_SERVER_ENABLED)
@EnableConfigServer
@Slf4j
@Configuration
public class ConfigServerAutoConfiguration {

  public static final String EXPS_CONFIG_SERVER_ENABLED =
      "${" + FrameworkConstants.PROP_SPRING_PREFIX + ".cloud.config.server.bootstrap:false}";

  /**
   * FIXME 修复错误：Cannot enhance @Configuration bean definition 'refreshScope' since its singleton
   * instance has beencreated too early.
   *
   * @return PropertySourcesPlaceholderConfigurer
   */
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
