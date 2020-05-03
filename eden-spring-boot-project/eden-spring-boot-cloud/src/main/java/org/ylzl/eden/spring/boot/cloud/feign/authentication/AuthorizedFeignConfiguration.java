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

package org.ylzl.eden.spring.boot.cloud.feign.authentication;

import feign.Feign;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.security.jwt.JwtProperties;
import org.ylzl.eden.spring.boot.security.oauth2.OAuth2Properties;

/**
 * 已认证的 Feign 配置
 *
 * @author gyl
 * @since 0.0.1
 */
@ConditionalOnClass({Feign.class})
@Slf4j
@Configuration
public class AuthorizedFeignConfiguration {

  @ConditionalOnMissingBean
  @Bean
  public RequestInterceptor authorizedRequestInterceptor(
      OAuth2Properties oAuth2Properties, JwtProperties jwtProperties) {
    return new AuthorizedRequestInterceptor(oAuth2Properties, jwtProperties);
  }
}
