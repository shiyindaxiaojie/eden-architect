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

package org.ylzl.eden.spring.boot.support.messaging.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerSeekAware;

import java.util.List;
import java.util.Map;

/**
 * 消费者启动监听适配器
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
public class ConsumerSeekAwareAdapter implements ConsumerSeekAware {

  public static final String MSG_SEED_TO_END_TOPICS =
      "Set Kafka topics seed to end on partitions assigned: {}";

  private List<String> seedToEndTopics;

  public ConsumerSeekAwareAdapter(List<String> seedToEndTopics) {
    this.seedToEndTopics = seedToEndTopics;
  }

  @Override
  public void registerSeekCallback(ConsumerSeekCallback consumerSeekCallback) {}

  @Override
  public void onPartitionsAssigned(
      Map<TopicPartition, Long> map, ConsumerSeekCallback consumerSeekCallback) {
    for (Map.Entry<TopicPartition, Long> entry : map.entrySet()) {
      TopicPartition topicPartition = entry.getKey();
      String topic = topicPartition.topic();
      if (!seedToEndTopics.contains(topic)) {
        continue;
      }
      int partition = topicPartition.partition();
      consumerSeekCallback.seek(topic, partition, Long.MAX_VALUE);
    }
    log.debug(MSG_SEED_TO_END_TOPICS, seedToEndTopics);
  }

  @Override
  public void onIdleContainer(
      Map<TopicPartition, Long> map, ConsumerSeekCallback consumerSeekCallback) {}
}
