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

package org.ylzl.eden.spring.boot.data.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.data.jdbc.namedparam.EnhancedNamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Jdbc 模板自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@AutoConfigureBefore({JdbcTemplateAutoConfiguration.class})
@Slf4j
@Configuration
public class EnhancedJdbcTemplateAutoConfiguration {

  public static final String MSG_INJECT_NAMED_JDBCTPL =
      "Autowired enhanced NamedParameterJdbcTemplate";

  /**
   * Inject EnhancedNamedParameterJdbcTemplate
   *
   * @param dataSource 数据源
   * @return EnhancedNamedParameterJdbcTemplate 实例
   */
  @ConditionalOnMissingBean
  @Bean
  public EnhancedNamedParameterJdbcTemplate enhancedNamedParameterJdbcTemplate(
      DataSource dataSource) {
    log.debug(MSG_INJECT_NAMED_JDBCTPL);
    return new EnhancedNamedParameterJdbcTemplate(dataSource);
  }
}
