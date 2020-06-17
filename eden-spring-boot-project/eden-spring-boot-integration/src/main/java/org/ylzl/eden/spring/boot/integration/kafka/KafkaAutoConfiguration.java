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

package org.ylzl.eden.spring.boot.integration.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerConfigUtils;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;
import org.ylzl.eden.spring.boot.integration.kafka.configurer.ConcurrentKafkaListenerContainerFactoryConfigurer;

/**
 * Kafka 配置
 *
 * @author gyl
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
@ConditionalOnClass({KafkaTemplate.class, EnableKafka.class})
@ConditionalOnExpression(KafkaAutoConfiguration.EXPS_KEFKA_ENABLED)
@EnableConfigurationProperties(KafkaProperties.class)
@Slf4j
@Configuration
public class KafkaAutoConfiguration {

  public static final String EXPS_KEFKA_ENABLED =
      "${" + FrameworkConstants.PROP_SPRING_PREFIX + ".kafka.enabled:true}";

  public static final String MSG_INJECT_KAFKA_TEMPLATE = "Autowired KafkaTemplate";

  private final KafkaProperties properties;

  public KafkaAutoConfiguration(KafkaProperties kafkaProperties) {
    this.properties = kafkaProperties;
  }

  @ConditionalOnMissingBean(KafkaTemplate.class)
  @Bean
  public KafkaTemplate<?, ?> kafkaTemplate(
      ProducerFactory<Object, Object> kafkaProducerFactory,
      ProducerListener<Object, Object> kafkaProducerListener) {
    log.debug(MSG_INJECT_KAFKA_TEMPLATE);
    KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
    kafkaTemplate.setProducerListener(kafkaProducerListener);
    kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
    return kafkaTemplate;
  }

  @ConditionalOnMissingBean(ProducerListener.class)
  @Bean
  public ProducerListener<Object, Object> kafkaProducerListener() {
    return new LoggingProducerListener<>();
  }

  @ConditionalOnMissingBean(ConsumerFactory.class)
  @Bean
  public ConsumerFactory<?, ?> kafkaConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(this.properties.buildConsumerProperties());
  }

  @ConditionalOnMissingBean(ProducerFactory.class)
  @Bean
  public ProducerFactory<?, ?> kafkaProducerFactory() {
    return new DefaultKafkaProducerFactory<>(this.properties.buildProducerProperties());
  }

  @ConditionalOnMissingBean
  @Bean
  public ConcurrentKafkaListenerContainerFactoryConfigurer
      kafkaListenerContainerFactoryConfigurer() {
    ConcurrentKafkaListenerContainerFactoryConfigurer configurer =
        new ConcurrentKafkaListenerContainerFactoryConfigurer();
    configurer.setKafkaProperties(properties);
    return configurer;
  }

  @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
  @Bean
  public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
      ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
      ConsumerFactory<Object, Object> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<Object, Object> containerFactory =
        new ConcurrentKafkaListenerContainerFactory();
    configurer.configure(containerFactory, consumerFactory);
    return containerFactory;
  }

  @EnableKafka
  @ConditionalOnMissingBean(
      name = KafkaListenerConfigUtils.KAFKA_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
  @Configuration
  protected static class EnableKafkaConfiguration {}
}
