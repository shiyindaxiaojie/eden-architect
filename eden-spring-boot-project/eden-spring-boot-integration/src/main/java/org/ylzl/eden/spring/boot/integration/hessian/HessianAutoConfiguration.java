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

package org.ylzl.eden.spring.boot.integration.hessian;

import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.ylzl.eden.spring.boot.integration.core.IntegrationConstants;

/**
 * Hessian 配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnClass(HessianProxy.class)
@ConditionalOnExpression(HessianAutoConfiguration.EXPS_HESSIAN_ENABLED)
@EnableConfigurationProperties(HessianProperties.class)
@Slf4j
@Configuration
public class HessianAutoConfiguration {

  public static final String EXPS_HESSIAN_ENABLED =
      "${" + IntegrationConstants.PROP_PREFIX + ".hessian.enabled:true}";

  @ConditionalOnMissingBean
  @Bean
  public HessianProxyFactoryBean hessianProxyFactoryBean() {
    return new HessianProxyFactoryBean();
  }

  @ConditionalOnMissingBean
  @Bean
  public HessianProxyFactory hessianProxyFactory() {
    return new HessianProxyFactory();
  }
}
