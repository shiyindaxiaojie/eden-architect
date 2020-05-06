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
package org.ylzl.eden.spring.boot.cloud.turbine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.actuator.HasFeatures;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.ChannelBindingServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.cloud.turbine.client.HystrixStreamAggregator;
import org.ylzl.eden.spring.boot.cloud.turbine.client.TurbineStreamClient;
import rx.subjects.PublishSubject;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Turbine Stream 自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@ConditionalOnClass(EnableBinding.class)
@ConditionalOnProperty(value = "turbine.stream.enabled")
@EnableBinding(TurbineStreamClient.class)
@EnableConfigurationProperties(TurbineStreamProperties.class)
@Configuration
public class TurbineStreamAutoConfiguration {

  @Autowired private ChannelBindingServiceProperties bindings;

  @Autowired private TurbineStreamProperties turbineStreamProperties;

  @PostConstruct
  public void init() {
    BindingProperties inputBinding = this.bindings.getBindings().get(TurbineStreamClient.INPUT);
    if (inputBinding == null) {
      this.bindings.getBindings().put(TurbineStreamClient.INPUT, new BindingProperties());
    }
    BindingProperties input = this.bindings.getBindings().get(TurbineStreamClient.INPUT);
    if (input.getDestination() == null) {
      input.setDestination(this.turbineStreamProperties.getDestination());
    }
    if (input.getContentType() == null) {
      input.setContentType(this.turbineStreamProperties.getContentType());
    }
  }

  @Bean
  public HystrixStreamAggregator hystrixStreamAggregator(
      ObjectMapper mapper, PublishSubject<Map<String, Object>> publisher) {
    return new HystrixStreamAggregator(mapper, publisher);
  }

  @Bean
  public HasFeatures Feature() {
    return HasFeatures.namedFeature("Turbine (Stream)", TurbineStreamProperties.class);
  }

  @Bean
  public PublishSubject<Map<String, Object>> hystrixSubject() {
    return PublishSubject.create();
  }
}
