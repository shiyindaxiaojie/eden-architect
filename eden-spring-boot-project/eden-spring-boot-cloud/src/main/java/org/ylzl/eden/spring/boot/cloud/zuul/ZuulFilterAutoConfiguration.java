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

package org.ylzl.eden.spring.boot.cloud.zuul;

import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.cloud.core.CloudConstants;
import org.ylzl.eden.spring.boot.cloud.zuul.filter.AccessControlFilter;
import org.ylzl.eden.spring.boot.cloud.zuul.filter.RateLimitingFilter;
import org.ylzl.eden.spring.boot.cloud.zuul.filter.ZuulFaultFilter;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;

/**
 * Zuul 过滤器自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnClass(ZuulFilter.class)
@EnableConfigurationProperties(ZuulProperties.class)
@Slf4j
@Configuration
public class ZuulFilterAutoConfiguration {

  public static final String EXPS_ACCESS_CONTROL_ENABLED =
      "${" + CloudConstants.PROP_PREFIX + ".zuul.access-control.enabled:true}";

  public static final String EXPS_RATE_LIMITING_ENABLED =
      "${" + CloudConstants.PROP_PREFIX + ".zuul.rate-limiting.enabled:false}";

  private static final String MSG_AUTOWIRED_ACCESS_CONTROL_FILTER = "Autowired Zuul AccessControl filter";

  private static final String MSG_AUTOWIRED_RATE_LIMIT_FILTER = "Autowired Zuul RateLimit filter";

  private static final String MSG_AUTOWIRED_ZUUL_FAULT_FILTER = "Autowired Zuul Fault filter";

  private final ZuulProperties zuulProperties;

  public ZuulFilterAutoConfiguration(ZuulProperties zuulProperties) {
    this.zuulProperties = zuulProperties;
  }

  @ConditionalOnBean(RouteLocator.class)
  @ConditionalOnExpression(EXPS_ACCESS_CONTROL_ENABLED)
  @ConditionalOnMissingBean
  @Bean
  public AccessControlFilter accessControlFilter(RouteLocator routeLocator) {
    log.debug(MSG_AUTOWIRED_ACCESS_CONTROL_FILTER);
    return new AccessControlFilter(zuulProperties, routeLocator);
  }

  @ConditionalOnExpression(EXPS_RATE_LIMITING_ENABLED)
  @ConditionalOnMissingBean
  @Bean
  public RateLimitingFilter rateLimitingFilter(
      @Value(FrameworkConstants.NAME_PATTERN) String applicationName) {
    log.debug(MSG_AUTOWIRED_RATE_LIMIT_FILTER);
    return new RateLimitingFilter(zuulProperties, applicationName);
  }

  @ConditionalOnMissingBean
  @Bean
  public ZuulFaultFilter zuulFaultFilter() {
    log.debug(MSG_AUTOWIRED_ZUUL_FAULT_FILTER);
    return new ZuulFaultFilter();
  }
}
