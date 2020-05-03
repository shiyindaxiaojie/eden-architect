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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.ylzl.eden.spring.boot.commons.lang.StringConstants;
import org.ylzl.eden.spring.boot.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.framework.core.util.Assert;
import org.ylzl.eden.spring.boot.security.core.enums.AuthenticationTypeEnum;
import org.ylzl.eden.spring.boot.security.oauth2.OAuth2Constants;
import org.ylzl.eden.spring.boot.security.oauth2.OAuth2Properties;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 令牌授权客户端适配器
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
public class TokenGrantClientAdapter implements TokenGrantClient {

  private static final String MSG_NONE_PROP_PASSWORD_CLIENT =
      "No client of password-grant configured in oauth2 properties";

  private static final String MSG_NONE_PROP_PASSWORD_SECRET =
      "No secret of password-grant configured in oauth2 properties";

  private static final String MSG_PASSWORD_GRANT =
      "Send password-grant to refresh token from OAuth Server, username = {}";

  private static final String MSG_NONE_PROP_REFRESH_CLIENT =
      "No client of refresh-token-grant configured in oauth2 properties";

  private static final String MSG_NONE_PROP_REFRESH_SECRET =
      "No secret of refresh-token-grant configured in oauth2 properties";

  private static final String MSG_REFRESH_TOKEN_GRANT =
      "Send refresh-token-grant to refresh token from OAuth Server, refreshToken = {}";

  private static final String MSG_NONE_PROP_CLIENT_CLIENT =
      "No client of client-credentials-grant configured in oauth2 properties";

  private static final String MSG_NONE_PROP_CLIENT_SECRET =
      "No secret of client-credentials-grant configured in oauth2 properties";

  private static final String MSG_CLIENT_GRANT =
      "Send client-credentials-grant to get token from OAuth Server";

  private final OAuth2AccessTokenClient oAuth2AccessTokenClient;

  private final OAuth2Properties.Authorization oAuth2Properties;

  public TokenGrantClientAdapter(
      OAuth2AccessTokenClient oAuth2AccessTokenClient, OAuth2Properties oAuth2Properties) {
    this.oAuth2AccessTokenClient = oAuth2AccessTokenClient;
    this.oAuth2Properties = oAuth2Properties.getAuthorization();
  }

  /**
   * 发送密码授权
   *
   * @param username 用户
   * @param password 密码
   * @return OAuth2 访问令牌
   */
  @Override
  public OAuth2AccessToken sendPasswordGrant(String username, String password) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.set(OAuth2Constants.GRANT_TYPE, OAuth2Constants.GRANT_TYPE_PASSWORD);
    params.set(OAuth2Constants.USERNAME, username);
    params.set(OAuth2Constants.PASSWORD, password);

    HttpHeaders headers = new HttpHeaders();
    this.addAuthentication(headers, params);
    String client = oAuth2Properties.getPassword().getClientId();
    Assert.hasText(client, MSG_NONE_PROP_PASSWORD_CLIENT);
    String secret = oAuth2Properties.getPassword().getClientSecret();
    Assert.hasText(secret, MSG_NONE_PROP_PASSWORD_SECRET);
    this.addAuthentication(headers, client, secret);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
    log.debug(MSG_PASSWORD_GRANT, username);
    return oAuth2AccessTokenClient.getOAuth2AccessToken(entity);
  }

  /**
   * 发送刷新授权
   *
   * @param refreshTokenValue 刷新令牌
   * @return OAuth2 访问令牌
   */
  @Override
  public OAuth2AccessToken sendRefreshGrant(String refreshTokenValue) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.REFRESH_TOKEN);
    params.add(OAuth2Constants.REFRESH_TOKEN, refreshTokenValue);

    HttpHeaders headers = new HttpHeaders();
    this.addAuthentication(headers, params);
    String client = oAuth2Properties.getRefreshToken().getClientId();
    Assert.hasText(client, MSG_NONE_PROP_REFRESH_CLIENT);
    String secret = oAuth2Properties.getRefreshToken().getClientSecret();
    Assert.hasText(secret, MSG_NONE_PROP_REFRESH_SECRET);
    this.addAuthentication(headers, client, secret);

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
    log.debug(MSG_REFRESH_TOKEN_GRANT, refreshTokenValue);
    return oAuth2AccessTokenClient.getOAuth2AccessToken(entity);
  }

  /**
   * 发送客户端授权
   *
   * @return OAuth2 访问令牌
   */
  @Override
  public OAuth2AccessToken sendClientCredentialsGrant() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.GRANT_TYPE_CLIENT_CREDENTIALS);

    HttpHeaders headers = new HttpHeaders();
    this.addAuthentication(headers, params);
    String client = oAuth2Properties.getClientCredentials().getClientId();
    Assert.hasText(client, MSG_NONE_PROP_CLIENT_CLIENT);
    String secret = oAuth2Properties.getClientCredentials().getClientSecret();
    Assert.hasText(secret, MSG_NONE_PROP_CLIENT_SECRET);
    this.addAuthentication(headers, client, secret);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
    log.debug(MSG_CLIENT_GRANT);
    return oAuth2AccessTokenClient.getOAuth2AccessToken(entity);
  }

  private void addAuthentication(
      HttpHeaders httpHeaders, MultiValueMap<String, String> formParams) {
    for (Map.Entry<String, List<String>> entry : formParams.entrySet()) {
      httpHeaders.add(entry.getKey(), StringUtils.join(entry.getValue(), StringConstants.COMMA));
    }
  }

  private void addAuthentication(HttpHeaders httpHeaders, String client, String secret) {
    String authorization = StringUtils.join(client, StringConstants.COLON, secret);
    String authorizationHeader =
        AuthenticationTypeEnum.BASIC_AUTH.getAuthorization(
            Base64Utils.encodeToString(authorization.getBytes(StandardCharsets.UTF_8)));
    httpHeaders.add(oAuth2Properties.getHeader(), authorizationHeader);
  }
}
