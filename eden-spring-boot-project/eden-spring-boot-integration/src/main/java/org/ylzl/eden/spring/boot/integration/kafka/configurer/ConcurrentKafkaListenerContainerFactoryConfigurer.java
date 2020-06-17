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

package org.ylzl.eden.spring.boot.integration.kafka.configurer;

import lombok.Setter;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.ylzl.eden.spring.boot.integration.kafka.KafkaProperties;

/**
 * Kafka 并发监听容器配置
 *
 * @author gyl
 * @since 1.0.0
 */
public class ConcurrentKafkaListenerContainerFactoryConfigurer {

  @Setter private KafkaProperties kafkaProperties;

  public void configure(
      ConcurrentKafkaListenerContainerFactory<Object, Object> listenerContainerFactory,
      ConsumerFactory<Object, Object> consumerFactory) {
    listenerContainerFactory.setConsumerFactory(consumerFactory);
    ContainerProperties containerProperties = listenerContainerFactory.getContainerProperties();
    KafkaProperties.Listener properties = this.kafkaProperties.getListener();
    if (properties.getAckMode() != null) {
      containerProperties.setAckMode(properties.getAckMode());
    }
    if (properties.getAckCount() != null) {
      containerProperties.setAckCount(properties.getAckCount());
    }
    if (properties.getAckTime() != null) {
      containerProperties.setAckTime(properties.getAckTime());
    }
    if (properties.getPollTimeout() != null) {
      containerProperties.setPollTimeout(properties.getPollTimeout());
    }
    if (properties.getConcurrency() != null) {
      listenerContainerFactory.setConcurrency(properties.getConcurrency());
      listenerContainerFactory.setBatchListener(true);
    }
  }
}
