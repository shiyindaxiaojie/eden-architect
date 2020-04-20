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
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;

/**
 * 嵌入式的 Kafka
 *
 * <p>Spring Kafka 从 1.X 升级到 2.X</p>
 * <ul>
 *     <li>org.springframework.kafka.test.rule.KafkaEmbedded 变更为 {@link EmbeddedKafkaRule}</li>
 * </ul>
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class EmbeddedKafka extends EmbeddedKafkaRule {

	private static final String MSG_STARTING = "Starting embedded kafka";

	private static final String MSG_STOPPING = "Stopping embedded kafka";

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

    @Override
    public void before() {
        log.debug(MSG_STARTING);
		super.before();
		closed = true;
    }

    @Override
    public void after() {
		log.debug(MSG_STOPPING);
        if (!isOpen()) {
			this.after();
        }
		closed = false;
    }

    public boolean isOpen() {
        return !closed;
    }
}
