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

package org.ylzl.eden.spring.boot.test.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.ylzl.eden.spring.boot.integration.kafka.KafkaProperties;

/**
 * 嵌入式的 Kafka
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
public class EmbeddedKafka extends KafkaEmbedded {

    private boolean closed = true;

    public EmbeddedKafka(int count) {
        super(count);
    }

    public EmbeddedKafka(int count, boolean controlledShutdown, String... topics) {
        super(count, controlledShutdown, topics);
    }

    public EmbeddedKafka(int count, boolean controlledShutdown, int partitions, String... topics) {
        super(count, controlledShutdown, partitions, topics);
    }

    public EmbeddedKafka(KafkaProperties kafkaProperties, String... topics) {
        super(1,true,  topics);
    }

    @Override
    public void before() {
        log.info("启动嵌入式的 Kakfa");
        try {
            String brokers = this.getBrokersAsString();
            System.setProperty("spring.kafka.bootstrap-servers", brokers);
            System.setProperty("spring.kafka.group-id", "test");
            log.info("Kafka Brokers: {}", brokers);
            super.before();
            closed = true;
        } catch (Exception e) {
            log.error("启动嵌入式的 Kafka 失败，异常：{}", e.getMessage(), e);
        }
    }

    @Override
    public void after() {
        if (!isOpen()) {
            log.info("嵌入式的 Kakfa 未启动，无需关闭");
            return;
        }
        log.info("关闭嵌入式的 Kakfa");
        this.after();
    }

    public boolean isOpen() {
        return !closed;
    }
}
