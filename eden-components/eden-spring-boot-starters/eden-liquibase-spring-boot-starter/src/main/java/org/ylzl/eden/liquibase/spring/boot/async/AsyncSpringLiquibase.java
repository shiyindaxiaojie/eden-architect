/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.liquibase.spring.boot.async;

import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.StopWatch;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 异步 Spring Liquibase
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class AsyncSpringLiquibase extends DataSourceClosingSpringLiquibase {

	private static final String STARTING_ASYNC = "Starting Liquibase asynchronously";
	private static final String STARTING_SYNC = "Starting Liquibase synchronously";
	private static final String EXCEPTION = "Liquibase could not start correctly, your database is not ready：{}";
	private static final String STARTED = "Liquibase has updated your database in {} ms";
	private static final String SLOWNESS = "Liquibase took more than {} seconds to start up!";

	private static final long SLOWNESS_THRESHOLD = 5;

	private final boolean aysnc;

	private final AsyncTaskExecutor executor;

	@Override
	public void afterPropertiesSet() throws LiquibaseException {
		if (aysnc) {
			try (Connection ignored = getDataSource().getConnection()) {
				executor.execute(
					() -> {
						try {
							log.debug(STARTING_ASYNC);
							initDb();
						} catch (LiquibaseException e) {
							log.error(EXCEPTION, e.getMessage(), e);
						}
					});
			} catch (SQLException e) {
				log.error(EXCEPTION, e.getMessage(), e);
			}
		} else {
			log.debug(STARTING_SYNC);
			initDb();
		}
	}

	protected void initDb() throws LiquibaseException {
		StopWatch watch = new StopWatch("liquibase");
		watch.start();
		super.afterPropertiesSet();
		watch.stop();
		log.debug(STARTED, watch.getTotalTimeMillis());
		if (watch.getTotalTimeMillis() > SLOWNESS_THRESHOLD * 1000L) {
			log.warn(SLOWNESS, SLOWNESS_THRESHOLD);
		}
	}
}
