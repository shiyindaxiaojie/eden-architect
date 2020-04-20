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

package org.ylzl.eden.spring.boot.data.flyway.async;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.StopWatch;
import org.ylzl.eden.spring.boot.framework.core.ProfileConstants;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * 异步 Flyway
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
public class AsyncFlyway extends Flyway {

	private static final String MSG_STARTING_ASYNC = "Starting Flyway asynchronously";

	private static final String MSG_STARTING_SYNC = "Starting Flyway synchronously";

	private static final String MSG_EXCEPTION = "Flyway could not start correctly, your database is not ready：{}";

	private static final String MSG_STARTED = "Flyway has updated your database in {} ms";

	private static final String MSG_SLOWNESS = "Flyway took more than {} seconds to start up!";

	private static final String STOP_WATCH_ID = "flyway";

    public static final long SLOWNESS_THRESHOLD = 5;

    private static final int MIGRATION_FAILED_COUNT = 0;

    private final AsyncTaskExecutor asyncTaskExecutor;

    private final Environment environment;

    public AsyncFlyway(AsyncTaskExecutor asyncTaskExecutor, Environment environment) {
        this.asyncTaskExecutor = asyncTaskExecutor;
        this.environment = environment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int migrate() throws FlywayException {
        if (environment.acceptsProfiles(ProfileConstants.SPRING_PROFILE_DEVELOPMENT)) {
            try (Connection ignored = getDataSource().getConnection()) {
                asyncTaskExecutor.submit(new Callable() {

                    @Override
                    public Integer call() {
                        try {
                            log.debug(MSG_STARTING_ASYNC);
                            return initDb();
                        } catch (FlywayException e) {
                            log.error(MSG_EXCEPTION, e.getMessage(), e);
                            return MIGRATION_FAILED_COUNT;
                        }
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
