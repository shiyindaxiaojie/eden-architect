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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.ylzl.eden.spring.framework.core.constant.SpringFrameworkConstants;
import org.ylzl.eden.spring.security.oauth2.token.TokenGrantClient;
import org.ylzl.eden.spring.security.oauth2.token.cookie.OAuth2CookieHelper;

/**
 * OAuth2 资源服务器配置适配器
 *
 * @author gyl
 * @since 1.0.0
 */
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class OAuth2ResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {

  private final TokenStore tokenStore;

  @Value(SpringFrameworkConstants.NAME_PATTERN)
  private String applicationName;

  @Autowired(required = false)
  private AuthenticationEntryPoint authenticationEntryPoint;

  @Autowired(required = false)
  private CorsFilter corsFilter;

  @Autowired(required = false)
  private TokenExtractor tokenExtractor;

  @Autowired(required = false)
  private TokenGrantClient tokenGrantClient;

  @Autowired(required = false)
  private OAuth2CookieHelper oAuth2CookieHelper;

  public OAuth2ResourceServerConfigurerAdapter(TokenStore tokenStore) {
    this.tokenStore = tokenStore;
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId(applicationName).tokenStore(tokenStore);

    if (tokenExtractor != null) {
      resources.tokenExtractor(tokenExtractor);
    }
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .headers()
        .frameOptions()
        .disable()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    if (authenticationEntryPoint != null) {
      http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

    if (corsFilter != null) {
      if (tokenGrantClient != null) {
        http.addFilterBefore(corsFilter, CorsFilter.class);
      } else {
        http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);
      }
    }

    if (tokenGrantClient != null) {
      http.apply(oAuth2SecurityConfigurer());
    }
  }

  protected OAuth2SecurityConfigurer oAuth2SecurityConfigurer() {
    return new OAuth2SecurityConfigurer(oAuth2CookieHelper, tokenStore, tokenGrantClient);
  }
}
