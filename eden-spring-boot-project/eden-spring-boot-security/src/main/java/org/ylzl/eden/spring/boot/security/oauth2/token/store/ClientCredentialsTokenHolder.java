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

package org.ylzl.eden.spring.boot.security.oauth2.token.store;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.ylzl.eden.spring.boot.security.oauth2.token.TokenGrantClient;
import org.ylzl.eden.spring.boot.security.oauth2.token.TokenProcessor;

/**
 * 客户端凭据令牌容器
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class ClientCredentialsTokenHolder {

  private final TokenProcessor tokenProcessor;
  private final TokenGrantClient tokenGrantClient;
  private OAuth2AccessToken oAuth2AccessToken;

  public ClientCredentialsTokenHolder(
      TokenProcessor tokenProcessor, TokenGrantClient tokenGrantClient) {
    this.tokenProcessor = tokenProcessor;
    this.tokenGrantClient = tokenGrantClient;
  }

  public OAuth2AccessToken get() {
    if (oAuth2AccessToken == null) {
      return retrieveNewAccessToken();
    }

    int exp = tokenProcessor.getExp(oAuth2AccessToken.getValue());
    int now = (int) (System.currentTimeMillis() / 1000L);
    if (exp < now) {
      return retrieveNewAccessToken();
    }

    return oAuth2AccessToken;
  }

  @Synchronized
  private OAuth2AccessToken retrieveNewAccessToken() {
    oAuth2AccessToken = tokenGrantClient.sendClientCredentialsGrant();
    return oAuth2AccessToken;
  }
}
