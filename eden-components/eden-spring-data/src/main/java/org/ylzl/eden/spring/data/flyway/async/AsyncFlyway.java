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

package org.ylzl.eden.spring.data.flyway.async;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.StopWatch;
import org.ylzl.eden.spring.framework.core.constant.SpringProfileConstants;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 异步 Flyway
 *
 * <p>变更日志：Flyway 升级到 6.X
 *
 * <ul>
 *   <li>{@code flyway.getDataSource()} 方法被移除
 *   <li>Flyway 构造函数新增 {@link Configuration} 为必须参数
 * </ul>
 *
 * @author gyl
 * @since 2.0.0
 */
@Slf4j
public class AsyncFlyway extends Flyway {

  public static final long SLOWNESS_THRESHOLD = 5;
  private static final String MSG_STARTING_ASYNC = "Starting Flyway asynchronously";
  private static final String MSG_STARTING_SYNC = "Starting Flyway synchronously";
  private static final String MSG_EXCEPTION =
      "Flyway could not start correctly, your database is not ready：{}";
  private static final String MSG_STARTED = "Flyway has updated your database in {} ms";
  private static final String MSG_SLOWNESS = "Flyway took more than {} seconds to start up!";
  private static final String STOP_WATCH_ID = "flyway";
  private static final int MIGRATION_FAILED_COUNT = 0;

  private final Configuration configuration;

  private final AsyncTaskExecutor asyncTaskExecutor;

  private final Environment environment;

  public AsyncFlyway(
      Configuration configuration, AsyncTaskExecutor asyncTaskExecutor, Environment environment) {
    super(configuration);
    this.configuration = configuration;
    this.asyncTaskExecutor = asyncTaskExecutor;
    this.environment = environment;
  }

  @Override
  public int migrate() throws FlywayException {
    if (environment.acceptsProfiles(Profiles.of(SpringProfileConstants.SPRING_PROFILE_DEVELOPMENT))) {
      try (Connection ignored = configuration.getDataSource().getConnection()) {
        asyncTaskExecutor.submit(
            () -> {
              try {
                log.debug(MSG_STARTING_ASYNC);
                return initDb();
              } catch (FlywayException e) {
                log.error(MSG_EXCEPTION, e.getMessage(), e);
                return MIGRATION_FAILED_COUNT;
              }
            });
      } catch (SQLException e) {
        log.error(MSG_EXCEPTION, e.getMessage(), e);
      }
    } else {
      log.debug(MSG_STARTING_SYNC);
      return initDb();
    }
    return MIGRATION_FAILED_COUNT;
  }

  protected int initDb() throws FlywayException {
    StopWatch watch = new StopWatch(STOP_WATCH_ID);
    watch.start();
    int migrationSuccessCount = super.migrate();
    watch.stop();
    log.debug(MSG_STARTED, watch.getTotalTimeMillis());
    if (watch.getTotalTimeMillis() > SLOWNESS_THRESHOLD * 1000L) {
      log.warn(MSG_SLOWNESS, SLOWNESS_THRESHOLD);
    }
    return migrationSuccessCount;
  }
}
