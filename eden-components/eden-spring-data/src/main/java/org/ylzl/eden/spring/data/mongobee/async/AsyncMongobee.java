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

package org.ylzl.eden.spring.data.mongobee.async;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.StopWatch;

/**
 * 异步 Mongobee
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class AsyncMongobee extends Mongobee {

	private static final String STARTING_ASYNC = "Starting Mongobee asynchronously";
	private static final String STARTING_SYNC = "Starting Mongobee synchronously";
	private static final String EXCEPTION = "Mongobee could not start correctly, your database is not ready: {}";
	private static final String STARTED = "Mongobee has updated your database in {} ms";
	private static final String SLOWNESS = "Mongobee took more than {} seconds to start up!";

	private static final long SLOWNESS_THRESHOLD = 5;

	private final boolean aysnc;

	private final AsyncTaskExecutor executor;

	public AsyncMongobee(boolean aysnc, AsyncTaskExecutor executor, MongoClient mongoClient) {
		super(mongoClient);
		this.aysnc = aysnc;
		this.executor = executor;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (aysnc) {
			executor.execute(
				() -> {
					try {
						log.debug(STARTING_ASYNC);
						initDb();
					} catch (Exception e) {
						log.error(EXCEPTION, e.getMessage(), e);
					}
				});
		} else {
			log.debug(STARTING_SYNC);
			initDb();
		}
	}

	protected void initDb() throws Exception {
		StopWatch watch = new StopWatch("mongobee");
		watch.start();
		super.afterPropertiesSet();
		watch.stop();
		log.debug(STARTED, watch.getTotalTimeMillis());
		if (watch.getTotalTimeMillis() > SLOWNESS_THRESHOLD * 1000L) {
			log.warn(SLOWNESS, SLOWNESS_THRESHOLD);
		}
	}
}
