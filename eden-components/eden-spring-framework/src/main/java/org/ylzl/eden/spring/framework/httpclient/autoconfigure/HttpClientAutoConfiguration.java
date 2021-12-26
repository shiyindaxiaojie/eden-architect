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

package org.ylzl.eden.spring.framework.httpclient.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.ylzl.eden.spring.framework.httpclient.env.HttpClientProperties;

/**
 * HttpClient 自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnClass(HttpClient.class)
@EnableConfigurationProperties(HttpClientProperties.class)
@Slf4j
@Configuration
public class HttpClientAutoConfiguration {

  private final HttpClientProperties httpClientProperties;

  public HttpClientAutoConfiguration(HttpClientProperties httpClientProperties) {
    this.httpClientProperties = httpClientProperties;
  }

  @ConditionalOnMissingBean
  @Bean
  public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
    SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
    clientHttpRequestFactory.setReadTimeout(httpClientProperties.getReadTimeout());
    clientHttpRequestFactory.setConnectTimeout(httpClientProperties.getConnectTimeout());
    return clientHttpRequestFactory;
  }
}
