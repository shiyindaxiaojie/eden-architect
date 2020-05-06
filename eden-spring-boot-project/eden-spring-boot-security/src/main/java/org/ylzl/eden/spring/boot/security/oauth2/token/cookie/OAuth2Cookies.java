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

package org.ylzl.eden.spring.boot.security.oauth2.token.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * OAuth2 Cookies
 *
 * @author gyl
 * @since 0.0.1
 */
public class OAuth2Cookies {

  private Cookie accessTokenCookie;

  private Cookie refreshTokenCookie;

  public Cookie getAccessTokenCookie() {
    return accessTokenCookie;
  }

  public Cookie getRefreshTokenCookie() {
    return refreshTokenCookie;
  }

  public void setCookies(Cookie accessTokenCookie, Cookie refreshTokenCookie) {
    this.accessTokenCookie = accessTokenCookie;
    this.refreshTokenCookie = refreshTokenCookie;
  }

  public void addCookiesTo(HttpServletResponse response) {
    response.addCookie(getAccessTokenCookie());
    response.addCookie(getRefreshTokenCookie());
  }
}
