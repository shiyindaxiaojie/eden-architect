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

package org.ylzl.eden.spring.security.oauth2.autoconfigure;

import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.spring.framework.web.RestAutoConfiguration;
import org.ylzl.eden.spring.security.core.constant.SpringSecurityConstants;
import org.ylzl.eden.spring.security.oauth2.endpoint.OAuth2TokenEndpoint;
import org.ylzl.eden.spring.security.oauth2.env.OAuth2Properties;
import org.ylzl.eden.spring.security.oauth2.token.*;
import org.ylzl.eden.spring.security.oauth2.token.cookie.OAuth2CookieHelper;
import org.ylzl.eden.spring.security.oauth2.token.cookie.OAuth2CookieTokenExtractor;
import org.ylzl.eden.spring.security.oauth2.token.jwt.JwtTokenProcessor;
import org.ylzl.eden.spring.security.oauth2.token.jwt.RemotedJwtAccessTokenConverter;
import org.ylzl.eden.spring.security.oauth2.token.jwt.SignatureVerifierClient;
import org.ylzl.eden.spring.security.oauth2.token.jwt.SignatureVerifierClientAdapter;
import org.ylzl.eden.spring.security.oauth2.token.store.ClientCredentialsTokenHolder;

/**
 * OAuth2 授权客户端自动配置
 *
 * @author gyl
 * @since 2.4.x
 */
@AutoConfigureAfter(RestAutoConfiguration.class)
@AutoConfigureBefore(OAuth2ResourceServerAutoConfiguration.class)
@ConditionalOnExpression(OAuth2AuthorizationClientAutoConfiguration.EXP_OAUTH2_AUTHORIZATION_CLIENT)
@EnableConfigurationProperties({OAuth2Properties.class})
@Slf4j
@Configuration
public class OAuth2AuthorizationClientAutoConfiguration {

  public static final String EXP_OAUTH2_AUTHORIZATION_CLIENT =
      "${" + SpringSecurityConstants.PROP_PREFIX + ".oauth2.authorization.client.enabled:false}";

  private static final String MSG_AUTOWIRED_OAUTH2_ACCESS_TOKEN_CLIENT =
      "Autowired OAuth2AccessTokenClient";

  private static final String MSG_AUTOWIRED_TOKEN_GRANT_CLIENT = "Autowired TokenGrantClient";

  private static final String MSG_AUTOWIRED_TOKEN_EXTRACTOR =
      "Autowired TokenExtractor (OAuth2CookieTokenExtractor)";

  private static final String MSG_AUTOWIRED_OAUTH2_COOKIE_HELPER = "Autowired OAuth2CookieHelper";

  private static final String MSG_AUTOWIRED_OAUTH2_TOKEN_ENDPOINT = "Autowired OAuth2TokenEndpoint";

  private static final String MSG_AUTOWIRED_CLIENT_CREDENTIALS_TOKEN_HOLDER =
      "Autowired ClientCredentialsTokenHolder";

  private final OAuth2Properties oAuth2Properties;

  public OAuth2AuthorizationClientAutoConfiguration(OAuth2Properties oAuth2Properties) {
    this.oAuth2Properties = oAuth2Properties;
  }

  @ConditionalOnMissingBean
  @Bean
  public OAuth2AccessTokenClient oAuth2AccessTokenClient(RestTemplate restTemplate) {
    log.debug(MSG_AUTOWIRED_OAUTH2_ACCESS_TOKEN_CLIENT);
    return new OAuth2AccessTokenClientAdapter(restTemplate, oAuth2Properties);
  }

  @ConditionalOnMissingBean
  @Bean
  public TokenGrantClient tokenGrantClient(OAuth2AccessTokenClient oAuth2AccessTokenClient) {
    log.debug(MSG_AUTOWIRED_TOKEN_GRANT_CLIENT);
    return new TokenGrantClientAdapter(oAuth2AccessTokenClient, oAuth2Properties);
  }

  @ConditionalOnMissingBean
  @Bean
  public TokenExtractor tokenExtractor() {
    log.debug(MSG_AUTOWIRED_TOKEN_EXTRACTOR);
    return new OAuth2CookieTokenExtractor();
  }

  @ConditionalOnMissingBean
  @Bean
  public OAuth2CookieHelper oAuth2CookieHelper(TokenProcessor tokenProcessor) {
    log.debug(MSG_AUTOWIRED_OAUTH2_COOKIE_HELPER);
    return new OAuth2CookieHelper(tokenProcessor, oAuth2Properties);
  }

  @ConditionalOnMissingBean
  @Bean
  public OAuth2TokenEndpoint oAuth2TokenEndpoint(
      TokenGrantClient tokenGrantClient, OAuth2CookieHelper oAuth2CookieHelper) {
    log.debug(MSG_AUTOWIRED_OAUTH2_TOKEN_ENDPOINT);
    return new OAuth2TokenEndpoint(tokenGrantClient, oAuth2CookieHelper);
  }

  @ConditionalOnMissingBean
  @Bean
  public ClientCredentialsTokenHolder clientCredentialsTokenHolder(
      TokenProcessor tokenProcessor, TokenGrantClient tokenGrantClient) {
    log.debug(MSG_AUTOWIRED_CLIENT_CREDENTIALS_TOKEN_HOLDER);
    return new ClientCredentialsTokenHolder(tokenProcessor, tokenGrantClient);
  }

  @ConditionalOnClass(Jwt.class)
  @Configuration
  public static class OAuth2AuthorizationClientJwtConfiguration {

    private static final String MSG_AUTOWIRED_SIGN_VERIFY_CLIENT =
        "Autowired SignatureVerifierClient";

    private static final String MSG_AUTOWIRED_JWT_ACCESS_TOKEN_CONVERTOR =
        "Autowired JwtAccessTokenConverter (OAuth2AuthorizationClient)";

    private static final String MSG_AUTOWIRED_TOKEN_STORE =
        "Autowired TokenStore (OAuth2AuthorizationClient JwtTokenStore)";

    private static final String MSG_AUTOWIRED_TOKEN_PROCESSOR =
        "Autowired TokenProcessor (JwtTokenProcessor)";

    private final OAuth2Properties oAuth2Properties;

    public OAuth2AuthorizationClientJwtConfiguration(OAuth2Properties oAuth2Properties) {
      this.oAuth2Properties = oAuth2Properties;
    }

    @ConditionalOnMissingBean
    @Bean
    public SignatureVerifierClient signatureVerifierClient(RestTemplate restTemplate) {
      log.debug(MSG_AUTOWIRED_SIGN_VERIFY_CLIENT);
      return new SignatureVerifierClientAdapter(restTemplate, oAuth2Properties);
    }

    @ConditionalOnMissingBean
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(
        SignatureVerifierClient signatureVerifierClient) {
      log.debug(MSG_AUTOWIRED_JWT_ACCESS_TOKEN_CONVERTOR);
      return new RemotedJwtAccessTokenConverter(signatureVerifierClient, oAuth2Properties);
    }

    @ConditionalOnMissingBean
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
      log.debug(MSG_AUTOWIRED_TOKEN_STORE);
      return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @ConditionalOnMissingBean
    @Bean
    public TokenProcessor tokenProcessor() {
      log.debug(MSG_AUTOWIRED_TOKEN_PROCESSOR);
      return new JwtTokenProcessor();
    }
  }
}
