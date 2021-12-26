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

package org.ylzl.eden.spring.security.oauth2.token.cookie;

import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Cookies 令牌提取器
 *
 * @author gyl
 * @since 1.0.0
 */
public class OAuth2CookieTokenExtractor extends BearerTokenExtractor {

  @Override
  protected String extractToken(HttpServletRequest request) {
    String result;
    Cookie accessTokenCookie = OAuth2CookieHelper.getAccessTokenCookie(request);
    if (accessTokenCookie != null) {
      result = accessTokenCookie.getValue();
    } else {
      result = super.extractToken(request);
    }
    return result;
  }
}
