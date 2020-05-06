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
package org.ylzl.eden.spring.boot.cloud.turbine.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.StringUtils;
import rx.subjects.PublishSubject;

import java.io.IOException;
import java.util.Map;

/**
 * Hystrix Stream 聚合器
 *
 * @author gyl
 * @since 0.0.1
 */
@CommonsLog
public class HystrixStreamAggregator {

  private ObjectMapper objectMapper;

  private PublishSubject<Map<String, Object>> subject;

  @Autowired
  public HystrixStreamAggregator(
      ObjectMapper objectMapper, PublishSubject<Map<String, Object>> subject) {
    this.objectMapper = objectMapper;
    this.subject = subject;
  }

  @SuppressWarnings("unchecked")
  @ServiceActivator(inputChannel = TurbineStreamClient.INPUT)
  public void sendToSubject(@Payload String payload) {
    if (payload.startsWith("\"")) {
      payload = payload.substring(1, payload.length() - 1);
      payload = payload.replace("\\\"", "\"");
    }
    try {
      Map<String, Object> map = this.objectMapper.readValue(payload, Map.class);
      Map<String, Object> data = getPayloadData(map);
      log.debug("Received hystrix stream payload: " + data);
      this.subject.onNext(data);
    } catch (IOException ex) {
      log.error("Error receiving hystrix stream payload: " + payload, ex);
    }
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> getPayloadData(Map<String, Object> jsonMap) {
    Map<String, Object> origin = (Map<String, Object>) jsonMap.get("origin");
    String instanceId = null;
    if (origin.containsKey("id")) {
      instanceId = origin.get("id").toString();
    }
    if (!StringUtils.hasText(instanceId)) {
      instanceId = origin.get("serviceId") + ":" + origin.get("host") + ":" + origin.get("port");
    }
    Map<String, Object> data = (Map<String, Object>) jsonMap.get("data");
    data.put("instanceId", instanceId);
    return data;
  }
}
