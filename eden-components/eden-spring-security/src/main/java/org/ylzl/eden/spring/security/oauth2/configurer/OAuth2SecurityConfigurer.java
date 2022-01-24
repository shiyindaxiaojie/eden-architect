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

package org.ylzl.eden.spring.security.oauth2.configurer;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.ylzl.eden.spring.security.oauth2.filter.OAuth2TokenFilter;
import org.ylzl.eden.spring.security.oauth2.token.TokenGrantClient;
import org.ylzl.eden.spring.security.oauth2.token.cookie.OAuth2CookieHelper;

/**
 * OAuth2 配置器
 *
 * @author gyl
 * @since 2.4.x
 */
public class OAuth2SecurityConfigurer
    extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private final OAuth2CookieHelper oAuth2CookieHelper;

  private final TokenStore tokenStore;

  private final TokenGrantClient tokenGrantClient;

  public OAuth2SecurityConfigurer(
      OAuth2CookieHelper oAuth2CookieHelper,
      TokenStore tokenStore,
      TokenGrantClient tokenGrantClient) {
    this.oAuth2CookieHelper = oAuth2CookieHelper;
    this.tokenStore = tokenStore;
    this.tokenGrantClient = tokenGrantClient;
  }

  @Override
  public void configure(HttpSecurity http) {
    OAuth2TokenFilter oAuth2TokenFilter =
        new OAuth2TokenFilter(oAuth2CookieHelper, tokenStore, tokenGrantClient);
    http.addFilterBefore(oAuth2TokenFilter, OAuth2AuthenticationProcessingFilter.class);
  }
}
