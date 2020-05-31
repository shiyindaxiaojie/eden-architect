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

package org.ylzl.eden.spring.boot.data.liquibase.async;

import liquibase.exception.LiquibaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.util.StopWatch;
import org.ylzl.eden.spring.boot.framework.core.ProfileConstants;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;

/**
 * 异步 Spring Liquibase
 *
 * @author gyl
 * @since 2.0.0
 */
@Slf4j
public class AsyncSpringLiquibase extends DataSourceClosingSpringLiquibase {

  private static final String MSG_STARTING_ASYNC = "Starting Liquibase asynchronously";

  private static final String MSG_STARTING_SYNC = "Starting Liquibase synchronously";

  private static final String MSG_EXCEPTION =
      "Liquibase could not start correctly, your database is not ready：{}";

  private static final String MSG_STARTED = "Liquibase has updated your database in {} ms";

  private static final String MSG_SLOWNESS = "Liquibase took more than {} seconds to start up!";

  private static final String STOP_WATCH_ID = "liquibase";

  public static final long SLOWNESS_THRESHOLD = 5;

  private final Executor asyncTaskExecutor;

  private final Environment environment;

  public AsyncSpringLiquibase(Executor asyncTaskExecutor, Environment environment) {
    this.asyncTaskExecutor = asyncTaskExecutor;
    this.environment = environment;
  }

  @Override
  public void afterPropertiesSet() throws LiquibaseException {
    if (environment.acceptsProfiles(Profiles.of(ProfileConstants.SPRING_PROFILE_DEVELOPMENT))) {
      try (Connection connection = getDataSource().getConnection()) {
        asyncTaskExecutor.execute(
            new Runnable() {

              @Override
              public void run() {
                try {
                  log.debug(MSG_STARTING_ASYNC);
                  initDb();
                } catch (LiquibaseException e) {
                  log.error(MSG_EXCEPTION, e.getMessage(), e);
                }
              }
            });
      } catch (SQLException e) {
        log.error(MSG_EXCEPTION, e.getMessage(), e);
      }
    } else {
      log.debug(MSG_STARTING_SYNC);
      initDb();
    }
  }

  protected void initDb() throws LiquibaseException {
    StopWatch watch = new StopWatch();
    watch.start(STOP_WATCH_ID);
    super.afterPropertiesSet();
    watch.stop();
    log.debug(MSG_STARTED, watch.getTotalTimeMillis());
    if (watch.getTotalTimeMillis() > SLOWNESS_THRESHOLD * 1000L) {
      log.warn(MSG_SLOWNESS, SLOWNESS_THRESHOLD);
    }
  }
}
