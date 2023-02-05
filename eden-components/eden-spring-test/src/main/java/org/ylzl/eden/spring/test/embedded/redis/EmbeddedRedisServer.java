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

import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.spring.test.embedded.EmbeddedServer;
import redis.embedded.RedisServer;
import redis.embedded.core.RedisServerBuilder;

import java.io.IOException;

/**
 * 嵌入式的 Redis Server
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class EmbeddedRedisServer implements EmbeddedServer {

	private static final String DEFAULT_BIND = "0.0.0.0";

	private static final int DEFAULT_PORT = 6379;

	private static final String DEFAULT_MAX_HEAP = "maxheap 64MB";

	private final RedisServerBuilder redisServerBuilder;

	private RedisServer redisServer;

	private boolean isRunning = false;

	public EmbeddedRedisServer() {
		this.redisServerBuilder = new RedisServerBuilder()
			.bind(DEFAULT_BIND)
			.port(DEFAULT_PORT)
			.setting(DEFAULT_MAX_HEAP);
	}

	/**
	 * 设置端口
	 *
	 * @param port 端口
	 * @return this
	 */
	@Override
	public EmbeddedServer port(int port) {
		redisServerBuilder.port(port);
		return this;
	}

	/**
	 * 设置密码
	 *
	 * @param password 密码
	 * @return this
	 */
	@Override
	public EmbeddedServer password(String password) {
		this.redisServerBuilder.setting("requirepass " + password);
		return this;
	}

	/**
	 * 启动
	 */
	@Override
	public void startup() {
		try {
			this.redisServer = redisServerBuilder.build();
			this.redisServer.start();
			this.isRunning = true;
		} catch (IOException e) {
			log.error("Startup embedded redis server error", e);
		}
	}

	/**
	 * 关闭
	 */
	@Override
	public void shutdown() {
		if (!isRunning()) {
			return;
		}
		try {
			this.redisServer.stop();
		} catch (IOException e) {
			log.error("Shutdown embedded redis server error", e);
		}
	}

	/**
	 * 是否在运行中
	 *
	 * @return 是否在运行中
	 */
	@Override
	public boolean isRunning() {
		return isRunning;
	}
}
