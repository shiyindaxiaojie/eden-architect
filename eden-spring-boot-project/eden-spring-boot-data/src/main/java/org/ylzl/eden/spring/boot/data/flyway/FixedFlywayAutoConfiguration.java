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

package org.ylzl.eden.spring.boot.data.flyway;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.AsyncTaskExecutor;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import org.ylzl.eden.spring.boot.data.flyway.async.AsyncFlyway;
import org.ylzl.eden.spring.boot.data.flyway.datasource.FlywaySQLiteDataSource;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;
import org.ylzl.eden.spring.boot.framework.scheduling.AsyncTaskExecutorAutoConfiguration;

import javax.sql.DataSource;
import java.util.List;

/**
 * Flyway 自动配置
 *
 * <p>变更日志：Spring Boot 升级 1.X 到 2.X
 *
 * <ul>
 *   <li>FlywayProperties 前缀从 {@code flyway} 变更为 {@code spring.flyway}
 * </ul>
 *
 * @author gyl
 * @since 2.0.0
 */
@AutoConfigureBefore(FlywayAutoConfiguration.class)
@ConditionalOnClass({Flyway.class})
@ConditionalOnProperty(
    prefix = FrameworkConstants.PROP_SPRING_PREFIX + ".flyway",
    name = "enabled",
    matchIfMissing = true)
@EnableConfigurationProperties({FlywayProperties.class})
@Slf4j
@Configuration
public class FixedFlywayAutoConfiguration {

  @AutoConfigureBefore(DataSourceAutoConfiguration.class)
  @ConditionalOnClass({DataSource.class})
  @Configuration
  public static class FixedFlywaySQLiteConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public DataSource fixedSQLiteDataSource(DataSourceProperties dataSourceProperties) {
      SQLiteConfig sqLiteConfig = new SQLiteConfig();
      sqLiteConfig.setJournalMode(SQLiteConfig.JournalMode.WAL);
      sqLiteConfig.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
      SQLiteDataSource dataSource = new SQLiteDataSource(sqLiteConfig);
      dataSource.setUrl(dataSourceProperties.getUrl());
      return new FlywaySQLiteDataSource(dataSource);
    }
  }

  @AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
  @ConditionalOnClass({AsyncTaskExecutor.class})
  @EnableConfigurationProperties(FlywayProperties.class)
  @Configuration
  public static class FlywayConfiguration extends FlywayAutoConfiguration.FlywayConfiguration {

    private static final String MSG_INJECT_FLYWAY = "Autowired Flyway";

    public static final String DEFAULT_LOCATIONS = "classpath:config/flyway/db/migration";

    private final AsyncTaskExecutor asyncTaskExecutor;

    private final Environment enviornment;

    private final List<String> locations;

    public FlywayConfiguration(
        @Qualifier(AsyncTaskExecutorAutoConfiguration.BEAN_TASK_EXECUTOR)
            AsyncTaskExecutor asyncTaskExecutor,
        Environment enviornment,
        @Value("${flyway.locations:" + DEFAULT_LOCATIONS + "}") List<String> locations) {
      this.asyncTaskExecutor = asyncTaskExecutor;
      this.enviornment = enviornment;
      this.locations = locations;
    }

    @Override
    public Flyway flyway(
        FlywayProperties properties,
        DataSourceProperties dataSourceProperties,
        ResourceLoader resourceLoader,
        ObjectProvider<DataSource> dataSource,
        @FlywayDataSource ObjectProvider<DataSource> flywayDataSource,
        ObjectProvider<FlywayConfigurationCustomizer> fluentConfigurationCustomizers,
        ObjectProvider<JavaMigration> javaMigrations,
        ObjectProvider<Callback> callbacks) {
      log.debug(MSG_INJECT_FLYWAY);
      // 覆盖默认的读取路径
      properties.setLocations(locations);
      Flyway flyway =
          super.flyway(
              properties,
              dataSourceProperties,
              resourceLoader,
              dataSource,
              flywayDataSource,
              fluentConfigurationCustomizers,
              javaMigrations,
              callbacks);
      return new AsyncFlyway(flyway.getConfiguration(), asyncTaskExecutor, enviornment);
    }
  }
}
