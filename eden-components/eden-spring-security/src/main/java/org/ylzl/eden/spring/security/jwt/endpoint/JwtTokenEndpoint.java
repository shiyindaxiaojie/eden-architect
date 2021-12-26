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

package org.ylzl.eden.spring.security.jwt.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ylzl.eden.spring.security.core.enums.AuthenticationTypeEnum;
import org.ylzl.eden.spring.security.core.login.Login;
import org.ylzl.eden.spring.security.jwt.constant.JwtConstants;
import org.ylzl.eden.spring.security.jwt.env.JwtProperties;
import org.ylzl.eden.spring.security.jwt.token.JwtToken;
import org.ylzl.eden.spring.security.jwt.token.JwtTokenService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * JWT 令牌服务端点
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
@RequestMapping
@RestController
public class JwtTokenEndpoint {

  private static final String MSG_AUTHENTICATE_USER = "JWT authenticate user: {}";

  private final JwtTokenService jwtTokenService;

  private final JwtProperties jwtProperties;

  public JwtTokenEndpoint(JwtTokenService jwtTokenService, JwtProperties jwtProperties) {
    this.jwtTokenService = jwtTokenService;
    this.jwtProperties = jwtProperties;
  }

  /**
   * 认证
   *
   * @param login 登录数据传输对象
   * @param response 响应对象
   * @return JWT 访问令牌
   */
  @PostMapping(value = JwtConstants.ENDPOINT_TOKEN)
  public ResponseEntity<JwtToken> authenticate(
      @Valid @RequestBody Login login, HttpServletResponse response) {
    log.info(MSG_AUTHENTICATE_USER, login.getUsername());
    JwtToken jwtToken = jwtTokenService.authenticate(login);
    response.addHeader(
        jwtProperties.getAuthorization().getHeader(),
        AuthenticationTypeEnum.BEARER_TOKEN.getAuthorization(jwtToken.getValue()));
    return ResponseEntity.ok(jwtToken);
  }
}
