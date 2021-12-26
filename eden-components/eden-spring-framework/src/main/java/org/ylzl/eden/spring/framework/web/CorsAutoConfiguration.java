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

package org.ylzl.eden.spring.framework.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.ylzl.eden.spring.framework.core.env.SpringFrameworkProperties;
import org.ylzl.eden.spring.framework.core.util.SpringPathMatcherConstants;
import org.ylzl.eden.spring.framework.web.filter.CorsFilterBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 跨域自动配置
 *
 * <p>从 Spring Boot 1.X 升级到 2.X
 *
 * <ul>
 *   <li>org.springframework.boot.actuate.autoconfigure.ManagementServerProperties 迁移到 {@link
 *       ManagementServerProperties}
 *   <li>{@code managementServerProperties.getContextPath()} 修改为 {@code
 *       managementServerProperties.getServlet().getContextPath()}
 * </ul>
 *
 * @author gyl
 * @since 2.0.0
 */
@Slf4j
@Configuration
public class CorsAutoConfiguration {

  public static final String MSG_AUTOWIRED_CORSFILTER = "Autowired CorsFilter";

  @ConditionalOnMissingBean
  @Bean
  public CorsFilter corsFilter(
      SpringFrameworkProperties springFrameworkProperties,
      ManagementServerProperties managementServerProperties) {
    log.debug(MSG_AUTOWIRED_CORSFILTER);
    CorsConfiguration corsConfiguration = springFrameworkProperties.getCors();
    List<String> paths = new ArrayList<>();
    if (corsConfiguration.getAllowedOrigins() != null
        && !corsConfiguration.getAllowedOrigins().isEmpty()) {
      paths.add(
          managementServerProperties.getServlet().getContextPath()
              + SpringPathMatcherConstants.ALL_CHILD_PATTERN);
    }
    return CorsFilterBuilder.builder().corsConfiguration(corsConfiguration).paths(paths).build();
  }
}
