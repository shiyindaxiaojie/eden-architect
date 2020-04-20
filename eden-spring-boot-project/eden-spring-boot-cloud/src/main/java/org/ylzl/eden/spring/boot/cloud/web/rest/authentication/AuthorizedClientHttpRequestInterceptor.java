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

package org.ylzl.eden.spring.boot.cloud.web.rest.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.ylzl.eden.spring.boot.security.core.util.SpringSecurityUtils;
import org.ylzl.eden.spring.boot.security.jwt.JwtProperties;
import org.ylzl.eden.spring.boot.security.oauth2.OAuth2Properties;

import java.io.IOException;

/**
 * 认证的客户端 HTTP 请求拦截器
 *
 * @author sion
 * @since 1.0.0
 */
public class AuthorizedClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2Properties oAuth2Properties;

    private final JwtProperties jwtProperties;

    public AuthorizedClientHttpRequestInterceptor(OAuth2Properties oAuth2Properties, JwtProperties jwtProperties) {
        this.oAuth2Properties = oAuth2Properties;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        Authentication authentication = SpringSecurityUtils.getAuthentication();
        if (authentication != null) {
            if (authentication.getDetails() instanceof OAuth2AuthenticationDetails && oAuth2Properties != null) {
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
                headers.add(oAuth2Properties.getAuthorization().getHeader(), SpringSecurityUtils.getAuthorizationHeader(details));
            } else if (authentication.getCredentials() instanceof String && jwtProperties != null) {
                String credentials = (String) authentication.getCredentials();
                headers.add(jwtProperties.getAuthorization().getHeader(), SpringSecurityUtils.getAuthorizationHeader(credentials));
            }
        }
        return execution.execute(request, body);
    }
}
