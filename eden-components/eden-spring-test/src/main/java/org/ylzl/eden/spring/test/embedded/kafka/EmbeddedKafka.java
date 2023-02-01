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

package org.ylzl.eden.spring.test.embedded.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.ylzl.eden.spring.test.embedded.EmbeddedServer;

/**
 * 嵌入式的 Kafka
 *
 * <p>Spring Kafka 从 1.X 升级到 2.X
 *
 * <ul>
 *   <li>org.springframework.kafka.test.rule.KafkaEmbedded 变更为 {@link EmbeddedKafkaRule}
 * </ul>
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class EmbeddedKafka implements EmbeddedServer {

	private static final int DEFAULT_PORT = 9092;

	private static final int DEFAULT_ZOOKEEPER_PORT = 2181;

	private static final int DEFAULT_BROKER_COUNT = 1;

	private final EmbeddedKafkaRule embeddedKafkaRule;

	private boolean isRunning = true;

	public EmbeddedKafka() {
		embeddedKafkaRule = new EmbeddedKafkaRule(DEFAULT_BROKER_COUNT);
		embeddedKafkaRule.kafkaPorts(DEFAULT_PORT);
		embeddedKafkaRule.zkPort(DEFAULT_ZOOKEEPER_PORT);
	}

	/**
	 * 设置端口
	 *
	 * @param port 端口
	 * @return this
	 */
	@Override
	public EmbeddedServer port(int port) {
		embeddedKafkaRule.kafkaPorts(port);
		return this;
	}

	/**
	 * 启动
	 */
	@Override
	public void startup() {
		embeddedKafkaRule.before();
		this.isRunning = true;
	}

	/**
	 * 关闭
	 */
	@Override
	public void shutdown() {
		if (!isRunning()) {
			return;
		}
		embeddedKafkaRule.after();
	}

	/**
	 * 是否在运行中
	 *
	 * @return 是否在运行中
	 */
	public boolean isRunning() {
		return !isRunning;
	}
}
