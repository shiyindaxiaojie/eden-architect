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

package org.ylzl.eden.spring.boot.data.liquibase;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncTaskExecutor;
import org.ylzl.eden.spring.boot.commons.bean.BeanCopier;
import org.ylzl.eden.spring.boot.data.liquibase.async.AsyncSpringLiquibase;
import org.ylzl.eden.spring.boot.framework.scheduling.AsyncTaskExecutorAutoConfiguration;

import javax.sql.DataSource;

/**
 * Liquibase 自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@AutoConfigureBefore(LiquibaseAutoConfiguration.class)
@ConditionalOnBean({DataSource.class, AsyncTaskExecutor.class})
@ConditionalOnClass({SpringLiquibase.class})
@ConditionalOnProperty(prefix = "liquibase", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({LiquibaseProperties.class})
@Slf4j
@Configuration
public class EnhancedLiquibaseAutoConfiguration {

  private static final String MSG_INJECT_LIQUIBASE = "Inject Liquibase";

  public static final String DEFAULT_CHANGE_LOG = "classpath:config/liquibase/master.xml";

  private final LiquibaseProperties liquibaseProperties;

  public EnhancedLiquibaseAutoConfiguration(LiquibaseProperties liquibaseProperties) {
    this.liquibaseProperties = liquibaseProperties;
  }

  @ConditionalOnMissingBean
  @Bean
  public SpringLiquibase liquibase(
      DataSource dataSource,
      Environment environment,
      @Qualifier(AsyncTaskExecutorAutoConfiguration.BEAN_TASK_EXECUTOR)
          AsyncTaskExecutor asyncTaskExecutor,
      @Value("${liquibase.change-log:" + DEFAULT_CHANGE_LOG + "}") String changeLog) {
    log.debug(MSG_INJECT_LIQUIBASE);
    SpringLiquibase liquibase = new AsyncSpringLiquibase(asyncTaskExecutor, environment);
    BeanCopier.copy(liquibaseProperties, liquibase);
    liquibase.setDataSource(dataSource);
    liquibase.setChangeLog(changeLog);
    return liquibase;
  }
}
