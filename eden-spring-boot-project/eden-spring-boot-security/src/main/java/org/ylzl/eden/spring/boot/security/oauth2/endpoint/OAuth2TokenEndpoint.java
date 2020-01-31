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

package org.ylzl.eden.spring.boot.security.oauth2.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ylzl.eden.spring.boot.commons.lang.BooleanUtils;
import org.ylzl.eden.spring.boot.security.core.login.Login;
import org.ylzl.eden.spring.boot.security.oauth2.OAuth2Constants;
import org.ylzl.eden.spring.boot.security.oauth2.token.TokenGrantClient;
import org.ylzl.eden.spring.boot.security.oauth2.token.cookie.OAuth2CookieHelper;
import org.ylzl.eden.spring.boot.security.oauth2.token.cookie.OAuth2Cookies;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * OAuth2 令牌端点
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
@RequestMapping
@RestController
public class OAuth2TokenEndpoint {

	private static final String MSG_LOGIN_USER = "OAuth2 Login user：{}";

	private static final String MSG_LOGOUT_USER = "OAuth2 Logout user：{}";

    private final TokenGrantClient tokenGrantClient;

    private final OAuth2CookieHelper oAuth2CookieHelper;

    public OAuth2TokenEndpoint(TokenGrantClient tokenGrantClient, OAuth2CookieHelper oAuth2CookieHelper) {
        this.tokenGrantClient = tokenGrantClient;
        this.oAuth2CookieHelper = oAuth2CookieHelper;
    }

    @PostMapping(value = OAuth2Constants.ENDPOINT_LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OAuth2AccessToken> login(@Valid @RequestBody Login login, HttpServletRequest request, HttpServletResponse response) {
        log.info(MSG_LOGIN_USER, login.getUsername());
        OAuth2AccessToken oAuth2AccessToken = tokenGrantClient.sendPasswordGrant(login.getUsername(), login.getPassword());

        boolean rememberMe = BooleanUtils.toBoolean(login.getRememberMe());
        OAuth2Cookies cookies = new OAuth2Cookies();
        oAuth2CookieHelper.createCookies(request, oAuth2AccessToken, rememberMe, cookies);
        cookies.addCookiesTo(response);
        return ResponseEntity.ok(oAuth2AccessToken);
    }

    @PostMapping(value = OAuth2Constants.ENDPOINT_LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info(MSG_LOGOUT_USER, SecurityContextHolder.getContext().getAuthentication().getName());
        oAuth2CookieHelper.clearCookies(request, response);
        return ResponseEntity.noContent().build();
    }
}
