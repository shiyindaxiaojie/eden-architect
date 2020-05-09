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

package org.ylzl.eden.spring.boot.framework.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.spring.boot.commons.env.CharsetConstants;

import java.util.List;

/**
 * REST 自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
@Configuration
public class RestAutoConfiguration {

  private static final String MSG_INJECT_REST_TEMPLATE = "装配 RestTemplate";

  private static final String MSG_INJECT_ASYNC_REST_TEMPLATE = "装配 AsyncRestTemplate";

  @ConditionalOnMissingBean
  @Bean
  public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
    log.debug(MSG_INJECT_REST_TEMPLATE);
    RestTemplate restTemplate = new RestTemplate(factory);
    this.setDefaultCharset(restTemplate.getMessageConverters());
    return restTemplate;
  }

  @ConditionalOnMissingBean
  @Bean
  public AsyncRestTemplate asyncRestTemplate(AsyncClientHttpRequestFactory factory) {
    log.debug(MSG_INJECT_ASYNC_REST_TEMPLATE);
    AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate(factory);
    this.setDefaultCharset(asyncRestTemplate.getMessageConverters());
    return asyncRestTemplate;
  }

  private void setDefaultCharset(List<HttpMessageConverter<?>> httpMessageConverters) {
    for (HttpMessageConverter<?> httpMessageConverter : httpMessageConverters) {
      if (httpMessageConverter instanceof StringHttpMessageConverter) {
        ((StringHttpMessageConverter) httpMessageConverter)
            .setDefaultCharset(CharsetConstants.UTF_8);
        break;
      }
    }
  }
}
