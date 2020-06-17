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

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.ylzl.eden.spring.boot.security.core.util.SpringSecurityUtils;
import org.ylzl.eden.spring.boot.security.jwt.JwtProperties;
import org.ylzl.eden.spring.boot.security.oauth2.OAuth2Properties;

/**
 * Feign 认证请求拦截器
 *
 * @author gyl
 * @since 1.0.0
 */
public class AuthorizedRequestInterceptor implements RequestInterceptor {

  private final OAuth2Properties oAuth2Properties;

  private final JwtProperties jwtProperties;

  public AuthorizedRequestInterceptor(
      OAuth2Properties oAuth2Properties, JwtProperties jwtProperties) {
    this.oAuth2Properties = oAuth2Properties;
    this.jwtProperties = jwtProperties;
  }

  @Override
  public void apply(RequestTemplate requestTemplate) {
    Authentication authentication = SpringSecurityUtils.getAuthentication();
    if (authentication != null) {
      if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
        OAuth2AuthenticationDetails details =
            (OAuth2AuthenticationDetails) authentication.getDetails();
        requestTemplate.header(
            oAuth2Properties.getAuthorization().getHeader(),
            SpringSecurityUtils.getAuthorizationHeader(details));
      } else if (authentication.getCredentials() instanceof String) {
        String credentials = (String) authentication.getCredentials();
        requestTemplate.header(
            jwtProperties.getAuthorization().getHeader(),
            SpringSecurityUtils.getAuthorizationHeader(credentials));
      }
    }
  }
}
