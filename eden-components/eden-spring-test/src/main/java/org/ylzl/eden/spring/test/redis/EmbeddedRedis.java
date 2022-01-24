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

package org.ylzl.eden.spring.test.redis;

import org.junit.rules.ExternalResource;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * 嵌入式的 Redis
 *
 * @author gyl
 * @since 2.4.x
 */
public class EmbeddedRedis extends ExternalResource {

	private static final int DEFAULT_PORT = 6379;

	private int port;

	private boolean suppressExceptions = false;

	private boolean closed = true;

	private RedisServer redisServer;

	public EmbeddedRedis() {
		port = DEFAULT_PORT;
	}

	public EmbeddedRedis(int port) {
		this.port = port;
	}

	public EmbeddedRedis(RedisProperties redisProperties) {
		this.port = redisProperties.getPort();
	}

	public static EmbeddedRedis runningAt(Integer port) {
		return new EmbeddedRedis(port != null ? port : DEFAULT_PORT);
	}

	@Override
	public void before() throws IOException {
		try {
			this.redisServer = new RedisServer(port);
			this.redisServer.start();
			closed = false;
		} catch (Exception e) {
			if (!suppressExceptions) {
				throw e;
			}
		}
	}

	@Override
	public void after() {
		if (!isOpen()) {
			return;
		}
		try {
			this.redisServer.stop();
			closed = true;
		} catch (Exception e) {
			if (!suppressExceptions) {
				throw e;
			}
		}
	}

	public boolean isOpen() {
		return !closed;
	}
}
