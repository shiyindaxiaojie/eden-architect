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

package org.ylzl.eden.spring.test.embedded.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.server.embedded.ZooKeeperServerEmbedded;
import org.ylzl.eden.spring.test.embedded.EmbeddedServer;

import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 嵌入式的 Zookeeper Server
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class EmbeddedZooKeeperServer implements EmbeddedServer {

	private static final int DEFAULT_PORT = 2181;

	private final ZooKeeperServerEmbedded zooKeeperServer;

	private boolean isRunning = true;

	public EmbeddedZooKeeperServer() {
		Properties configuration = new Properties();
		configuration.put("clientPort", String.valueOf(DEFAULT_PORT));
		configuration.put("host", "localhost");
		configuration.put("ticktime", "2000");
		configuration.put("initLimit", "10");
		configuration.put("syncLimit", "5");
		configuration.put("dataDir", "/tmp/zookeeper");
//		configuration.put("admin.serverPort", "8181");
//		configuration.put("metricsProvider.className", "org.apache.zookeeper.metrics.prometheus.PrometheusMetricsProvider");
//		configuration.put("metricsProvider.httpPort", "7000");
//		configuration.put("metricsProvider.exportJvmInfo", "true");
		try {
			String path = EmbeddedZooKeeperServer.class.getClassLoader().getResource("").toURI().toString();
			URI uri = new URL(path).toURI();
			this.zooKeeperServer = ZooKeeperServerEmbedded.builder()
				.baseDir(Paths.get(uri))
				.configuration(configuration)
				.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置端口
	 *
	 * @param port 端口
	 * @return this
	 */
	@Override
	public EmbeddedServer port(int port) {
		return this;
	}

	/**
	 * 启动
	 */
	@Override
	public void startup() {
		try {
			this.zooKeeperServer.start();
			this.isRunning = true;
		} catch (Exception e) {
			log.error("Startup embedded zookeeper server error", e);
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
			this.zooKeeperServer.close();
		} catch (Exception e) {
			log.error("Shutdown embedded zookeeper server error", e);
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
