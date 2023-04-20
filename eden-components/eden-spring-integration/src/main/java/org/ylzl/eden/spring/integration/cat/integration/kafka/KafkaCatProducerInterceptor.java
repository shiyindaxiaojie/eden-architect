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

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.ylzl.eden.spring.integration.cat.CatConstants;

import java.util.Map;

/**
 * Kafka 发送消息切入 CAT 埋点
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class KafkaCatProducerInterceptor<K, V> implements ProducerInterceptor<K, V> {

	private Map<String, ?> configs;

	@Override
	public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
		return record;
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		long latency = System.currentTimeMillis() - metadata.timestamp();

		String name = metadata.topic();
		Transaction transaction = Cat.newTransaction(CatConstants.TYPE_MQ_PRODUCER, name);
		try {
			transaction.addData(CatConstants.DATA_COMPONENT, CatConstants.DATA_COMPONENT_KAFKA);
			transaction.setDurationInMillis(latency);

			Cat.logEvent(CatConstants.TYPE_MQ_PRODUCER_BROKER, String.valueOf(configs.get("bootstrap.servers")));
			Cat.logEvent(CatConstants.TYPE_MQ_PRODUCER_GROUP, String.valueOf(configs.get("")));

			if (exception == null) {
				transaction.setSuccessStatus();
			} else {
				transaction.setStatus(exception.getCause());
				Cat.logError(exception.getMessage(), exception);
			}
		} catch (Exception e) {
			transaction.setStatus(e);
			Cat.logError(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			transaction.complete();
		}
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {
		this.configs = configs;
	}
}
