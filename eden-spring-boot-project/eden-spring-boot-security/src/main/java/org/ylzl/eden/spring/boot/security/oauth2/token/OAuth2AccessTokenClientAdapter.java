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

package org.ylzl.eden.spring.boot.security.oauth2.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.spring.boot.framework.web.rest.errors.UnauthorizedException;
import org.ylzl.eden.spring.boot.security.oauth2.OAuth2Properties;

/**
 * OAuth2 访问令牌提供器
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class OAuth2AccessTokenClientAdapter implements OAuth2AccessTokenClient {

  private static final String MSG_REQ_TOKEN_FAILED =
      "Request token failed from OAuth Server, statue code: {}";

  private static final String MSG_REQ_TOKEN_EXCEPTION =
      "Request token failed from OAuth Server, caught exception: {}";

  private static final String MSG_NONE_PROP_TOKEN_URI =
      "No access-token-uri configured in application properties";

  private final RestTemplate restTemplate;

  private final OAuth2Properties.Authorization oAuth2Properties;

  public OAuth2AccessTokenClientAdapter(
      RestTemplate restTemplate, OAuth2Properties oAuth2Properties) {
    this.restTemplate = restTemplate;
    this.oAuth2Properties = oAuth2Properties.getAuthorization();
  }

  /**
   * 获取 OAuth2 访问令牌
   *
   * @param entity 请求实体
   * @return OAuth2 访问令牌
   */
  @Override
  public OAuth2AccessToken getOAuth2AccessToken(HttpEntity<MultiValueMap<String, String>> entity) {
    try {
      String tokenUri = getAccessTokenUri();
      ResponseEntity<OAuth2AccessToken> responseEntity =
          restTemplate.postForEntity(tokenUri, entity, OAuth2AccessToken.class);
      if (!responseEntity.getStatusCode().is2xxSuccessful()) {
        log.warn(MSG_REQ_TOKEN_FAILED, responseEntity.getStatusCodeValue());
        throw new HttpClientErrorException(responseEntity.getStatusCode());
      }
      return responseEntity.getBody();
    } catch (HttpClientErrorException e) {
      log.error(MSG_REQ_TOKEN_EXCEPTION, e.getMessage(), e);
      if (e.getStatusCode().is4xxClientError()) {
        throw new UnauthorizedException();
      }
      throw e;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new InvalidGrantException(e.getMessage());
    }
  }

  /**
   * 获取 OAuth2 访问令牌端点
   *
   * @return OAuth2 访问令牌端点
   */
  protected String getAccessTokenUri() {
    String tokenUri = oAuth2Properties.getAccessTokenUri();
    if (tokenUri == null) {
      throw new InvalidClientException(MSG_NONE_PROP_TOKEN_URI);
    }
    return tokenUri;
  }
}
