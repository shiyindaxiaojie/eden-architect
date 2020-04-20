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

package org.ylzl.eden.spring.boot.test.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.rules.ExternalResource;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import redis.embedded.RedisServer;

/**
 * 嵌入式的 Redis
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class EmbeddedRedisServer extends ExternalResource {

	private static final String MSG_STARTING = "Starting embedded redis server";

	private static final String MSG_STOPPING = "Stopping embedded redis server";

	private static final int DEFAULT_PORT = 6379;

	private int port;

	private boolean closed = true;

	private RedisServer redisServer;

	public EmbeddedRedisServer() {
		port = DEFAULT_PORT;
	}

	public EmbeddedRedisServer(int port) {
		this.port = port;
	}

	public EmbeddedRedisServer(RedisProperties redisProperties) {
		this.port = redisProperties.getPort();
	}

	public static EmbeddedRedisServer runningAt(Integer port) {
		return new EmbeddedRedisServer(port != null ? port : DEFAULT_PORT);
	}

	@Override
	public void before() {
		log.debug(MSG_STARTING);
		this.redisServer = new RedisServer(port);
		this.redisServer.start();
		closed = false;
	}

	@Override
	public void after() {
		log.debug(MSG_STOPPING);
		if (!isOpen()) {
			this.redisServer.stop();
		}
		closed = true;
	}

	public boolean isOpen() {
		return !closed;
	}
}
