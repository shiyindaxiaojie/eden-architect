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

package org.ylzl.eden.spring.security.jwt.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.ylzl.eden.spring.security.jwt.constant.JwtConstants;
import org.ylzl.eden.spring.security.jwt.env.JwtProperties;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.text.MessageFormat;
import java.util.Date;

/**
 * JWT 令牌提供器
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class JwtTokenProvider {

  private static final String MSG_INVALID_SIGNATURE =
      "JWT signature are invalid, caught exception: {0}";

  private static final String MSG_INVALID_TOKEN = "JWT token are invalid, caught exception: {0}";

  private static final String MSG_EXPIRED_TOKEN = "JWT token are expired, caught exception: {0}";

  private static final String MSG_UNSUPPORTED_TOKEN =
      "JWT token are unsupported, caught exception: {0}";

  private static final String MSG_HANDLE_EXCEPTION =
      "JWT token compact of handler are invalid, caught exception: {0}";
  private final JwtProperties.Authentication jwtProperties;
  private String secretKey;
  private long tokenValidityInMilliseconds;
  private long tokenValidityInMillisecondsForRememberMe;

  public JwtTokenProvider(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties.getAuthentication();
  }

  /** 初始化 JWT 配置 */
  @PostConstruct
  public void init() {
    this.secretKey =
        jwtProperties.getBase64Secret() != null
            ? jwtProperties.getBase64Secret()
            : Base64Utils.encodeToString(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    this.tokenValidityInMilliseconds = 1000 * jwtProperties.getTokenValidityInSeconds();
    this.tokenValidityInMillisecondsForRememberMe =
        1000 * jwtProperties.getTokenValidityInSecondsForRememberMe();
  }

  /**
   * 构建令牌
   *
   * @param subject 主题
   * @param issuer 签发者
   * @param claim 自定义串
   * @param expiration 有效期
   * @return 令牌
   */
  public String build(String subject, String issuer, String claim, Date expiration) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    Key key = Keys.hmacShaKeyFor(keyBytes);
    return Jwts.builder()
        .setSubject(subject)
        .claim(JwtConstants.CLAIM_KEY, claim)
        .setIssuedAt(new Date())
        .setIssuer(issuer)
        .setExpiration(expiration)
        .signWith(key)
        .compact();
  }

  /**
   * 构建令牌
   *
   * @param subject 主题
   * @param issuer 签发者
   * @param claim 自定义串
   * @param rememberMe 记住我
   * @return 令牌
   */
  public String build(String subject, String issuer, String claim, boolean rememberMe) {
    long now = (new Date()).getTime();
    Date expiration;
    if (rememberMe) {
      expiration = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
    } else {
      expiration = new Date(now + this.tokenValidityInMilliseconds);
    }
    return build(subject, issuer, claim, expiration);
  }

  /**
   * 解析令牌
   *
   * @param token
   * @return
   * @throws SecurityException JWT 签名异常
   * @throws MalformedJwtException JWT 令牌无效
   * @throws ExpiredJwtException JWT 令牌失效
   * @throws UnsupportedJwtException 不支持的 JWT 令牌
   * @throws IllegalArgumentException 处理 JWT 令牌异常
   */
  public Claims parse(String token)
      throws SecurityException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException,
          IllegalArgumentException {
    try {
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    } catch (SecurityException e) {
      throw new SecurityException(MessageFormat.format(MSG_INVALID_SIGNATURE, e.getMessage()), e);
    } catch (MalformedJwtException e) {
      throw new MalformedJwtException(MessageFormat.format(MSG_INVALID_TOKEN, e.getMessage()), e);
    } catch (ExpiredJwtException e) {
      throw new ExpiredJwtException(
          null, null, MessageFormat.format(MSG_EXPIRED_TOKEN, e.getMessage()), e);
    } catch (UnsupportedJwtException e) {
      throw new UnsupportedJwtException(
          MessageFormat.format(MSG_UNSUPPORTED_TOKEN, e.getMessage()), e);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          MessageFormat.format(MSG_HANDLE_EXCEPTION, e.getMessage()), e);
    }
  }
}
