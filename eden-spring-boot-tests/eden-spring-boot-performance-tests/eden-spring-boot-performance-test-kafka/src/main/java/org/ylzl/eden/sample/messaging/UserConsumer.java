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

package org.ylzl.eden.sample.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.ylzl.eden.sample.domain.User;
import org.ylzl.eden.sample.repository.jdbc.UserJdbc;
import org.ylzl.eden.spring.boot.commons.json.JacksonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 消费者
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
@Component
public class UserConsumer {

	private final UserJdbc userJdbc;

	public UserConsumer(UserJdbc userJdbc) {
		this.userJdbc = userJdbc;
	}

	// 如果 spring-kafka 的版本小于 1.1.1，不支持设置以逗号隔开的多个主题，需要更换为 @KafkaListener(topics = "#{T(org.ylzl.commons.lang.StringUtils.toStringList('${spring.kafka.consumer.topics}', ','))}") 实现
	// spring-kafka 的版本在 1.1.1 以前不支持批量消费，参数只能用 ConsumerRecord<String, String> record
	@KafkaListener(topics = "${spring.kafka.template.default-topic}")
	public void consume(List<ConsumerRecord<String, String>> records, Acknowledgment ack) throws IOException {
		// records 的大小为 spring.kafka.consumer.max-poll-records
		// 注意：在一个监听方法配置多个 Topic，每次拉取的 records 包含多个 topic
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start("consume");
		/*int capacity = records.size();
		List<User> users = new ArrayList<>(capacity);
		for (ConsumerRecord record: records) {
			// 主题：record.topic()
			// 消息：record.value()

			// Kakfa 默认自动提交 Offset
			// 如果设置了 spring.kafka.consumer.enable-auto-commit 为 false，需要执行 ack.acknowledge(); 手动提交 Offset
			users.add(JacksonUtils.toObject(record.value().toString(), User.class));
		}
		userJdbc.batchInsert(users, 1000);*/
//		stopWatch.stop();
//		log.debug("批次消费大小为 {}，耗时为 {} 秒", capacity, stopWatch.getTotalTimeSeconds());
		ack.acknowledge(); // 手动提交，需要设置提交模式为 MANUAL_IMMEDIATE
	}
}
