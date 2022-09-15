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
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.StopWatch;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProfileConstants;

/**
 * 异步 Mongobee
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class AsyncMongobee extends Mongobee {

	public static final long SLOWNESS_THRESHOLD = 5;
	private static final String MSG_STARTING_ASYNC = "Starting Mongobee asynchronously";
	private static final String MSG_STARTING_SYNC = "Starting Mongobee synchronously";
	private static final String MSG_EXCEPTION =
		"Mongobee could not start correctly, your database is not ready：{}";
	private static final String MSG_STARTED = "Mongobee has updated your database in {} ms";
	private static final String MSG_SLOWNESS = "Mongobee took more than {} seconds to start up!";
	private static final String STOP_WATCH_ID = "mongobee";
	private final TaskExecutor taskExecutor;

	private final Environment environment;

	public AsyncMongobee(
		MongoClient mongoClient, Environment environment, TaskExecutor taskExecutor) {
		super(mongoClient);
		this.environment = environment;
		this.taskExecutor = taskExecutor;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (environment.acceptsProfiles(
			Profiles.of(SpringProfileConstants.SPRING_PROFILE_DEVELOPMENT))) {
			taskExecutor.execute(
				new Runnable() {

					@Override
					public void run() {
						try {
							log.debug(MSG_STARTING_ASYNC);
							initDb();
						} catch (Exception e) {
							log.error(MSG_EXCEPTION, e.getMessage(), e);
						}
					}
				});
		} else {
			log.debug(MSG_STARTING_SYNC);
			initDb();
		}
	}

	protected void initDb() throws Exception {
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
