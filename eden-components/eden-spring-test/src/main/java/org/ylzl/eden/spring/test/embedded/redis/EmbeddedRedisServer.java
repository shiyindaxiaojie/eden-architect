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

package org.ylzl.eden.spring.test.embedded.redis;

import org.junit.rules.ExternalResource;
import redis.embedded.RedisServer;
import redis.embedded.core.RedisServerBuilder;

/**
 * 嵌入式的 Redis Server
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EmbeddedRedisServer extends ExternalResource {

	private static final String DEFAULT_BIND = "0.0.0.0";

	private static final int DEFAULT_PORT = 6379;

	private static final String DEFAULT_MAX_HEAP = "maxheap 256MB";

	private final RedisServerBuilder redisServerBuilder;

	private RedisServer redisServer;

	private boolean closed = true;

	public EmbeddedRedisServer() {
		this.redisServerBuilder = new RedisServerBuilder()
			.bind(DEFAULT_BIND)
			.port(DEFAULT_PORT)
			.setting(DEFAULT_MAX_HEAP);
	}

	public EmbeddedRedisServer(int port) {
		this.redisServerBuilder = new RedisServerBuilder()
			.bind(DEFAULT_BIND)
			.port(port)
			.setting(DEFAULT_MAX_HEAP);
	}

	public void addConfigLine(String configLine) {
		this.redisServerBuilder.setting(configLine);
	}

	public void addRequirePass(String requirepass) {
		this.redisServerBuilder.setting("requirepass " + requirepass);
	}

	@Override
	public void before() {
		try {
			this.redisServer = redisServerBuilder.build();
			this.redisServer.start();
			closed = false;
		} catch (Exception e) {
			throw new RuntimeException(e);
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
			throw new RuntimeException(e);
		}
	}

	public boolean isOpen() {
		return !closed;
	}
}
