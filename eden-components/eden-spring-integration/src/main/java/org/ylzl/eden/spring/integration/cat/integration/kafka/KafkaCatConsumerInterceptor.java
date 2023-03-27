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

package org.ylzl.eden.spring.integration.cat.integration.kafka;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

/**
 * Kafka 消费消息切入 CAT 埋点
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class KafkaCatConsumerInterceptor<K, V> implements ConsumerInterceptor<K, V> {

	private Map<String, ?> configs;

	@Override
	public ConsumerRecords<K, V> onConsume(ConsumerRecords<K, V> records) {
		return records;
	}

	@Override
	public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
		// TODO
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {
		this.configs = configs;
	}
}
