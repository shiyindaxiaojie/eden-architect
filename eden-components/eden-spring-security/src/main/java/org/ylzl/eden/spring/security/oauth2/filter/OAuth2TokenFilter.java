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

package org.ylzl.eden.spring.security.oauth2.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.GenericFilterBean;
import org.ylzl.eden.spring.framework.web.cookie.CookieCollection;
import org.ylzl.eden.spring.framework.web.cookie.CookiesHttpServletRequestWrapper;
import org.ylzl.eden.spring.security.oauth2.token.TokenGrantClient;
import org.ylzl.eden.spring.security.oauth2.token.cookie.OAuth2CookieHelper;
import org.ylzl.eden.spring.security.oauth2.token.cookie.OAuth2Cookies;
import org.ylzl.eden.spring.security.oauth2.token.store.PersistentTokenCache;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OAuth2 令牌过滤器
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class OAuth2TokenFilter extends GenericFilterBean {

  private static final long REFRESH_TOKEN_VALIDITY_MILLIS = 10_000L;

  private static final int REFRESH_WINDOW_SECS = 30;

  private static final String MSG_REFRESH_TOKEN_EXCEPTION = "刷新 OAuth2 访问令牌异常：{}";

  private static final String MSG_UNAUTHORIZED_CLIENT_EXCEPTION =
      "Could not refresh OAuth2 token due to unauthorized";

  private static final String MSG_ACCESS_TOKEN_WITHOUT_REFRESH_TOKEN =
      "OAuth2 access token has expired, but there's no refresh token";

  private static final String MSG_EXPIRED_ACCESS_TOKEN = "OAuth2 访问令牌已过期";

  private static final String MSG_SESSION_EXPIRED_DUE_INACTIVITY = "OAuth2 会话因不活动过期";

  private static final String MSG_REUSE_CACHE_REFRESH_TOKEN = "复用缓存的 OAuth2 刷新令牌认证";

  private final OAuth2CookieHelper oAuth2CookieHelper;

  private final TokenStore tokenStore;

  private final TokenGrantClient tokenGrantClient;

  private final PersistentTokenCache<OAuth2Cookies> recentlyRefreshedTokenCache;

  public OAuth2TokenFilter(
      OAuth2CookieHelper oAuth2CookieHelper,
      TokenStore tokenStore,
      TokenGrantClient tokenGrantClient) {
    this.oAuth2CookieHelper = oAuth2CookieHelper;
    this.tokenStore = tokenStore;
    this.tokenGrantClient = tokenGrantClient;
    this.recentlyRefreshedTokenCache = new PersistentTokenCache<>(REFRESH_TOKEN_VALIDITY_MILLIS);
  }

  /**
   * 执行过滤
   *
   * @param servletRequest
   * @param servletResponse
   * @param filterChain
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    try {
      httpServletRequest = refreshTokensIfExpiring(httpServletRequest, httpServletResponse);
    } catch (ClientAuthenticationException ex) {
      log.warn(MSG_REFRESH_TOKEN_EXCEPTION, ex.getMessage(), ex);
      httpServletRequest = this.stripTokens(httpServletRequest);
    }
    filterChain.doFilter(httpServletRequest, servletResponse);
  }

  /**
   * 如果令牌未过期，刷新令牌
   *
   * @param httpServletRequest
   * @param httpServletResponse
   * @return
   */
  public HttpServletRequest refreshTokensIfExpiring(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    HttpServletRequest newHttpServletRequest = httpServletRequest;
    Cookie accessTokenCookie = OAuth2CookieHelper.getAccessTokenCookie(httpServletRequest);
    if (mustRefreshToken(accessTokenCookie)) {
      Cookie refreshCookie = OAuth2CookieHelper.getRefreshTokenCookie(httpServletRequest);
      if (refreshCookie != null) {
        try {
          newHttpServletRequest =
              this.refreshToken(httpServletRequest, httpServletResponse, refreshCookie);
        } catch (HttpClientErrorException ex) {
          throw new UnauthorizedClientException(MSG_UNAUTHORIZED_CLIENT_EXCEPTION, ex);
        }
      } else if (accessTokenCookie != null) {
        // 如果刷新令牌不存在，需要判断访问令牌是否已失效
        // 如果访问令牌已失效，抛出访问令牌过期的异常
        log.warn(MSG_ACCESS_TOKEN_WITHOUT_REFRESH_TOKEN);
        OAuth2AccessToken token = tokenStore.readAccessToken(accessTokenCookie.getValue());
        if (token.isExpired()) {
          throw new InvalidTokenException(MSG_EXPIRED_ACCESS_TOKEN);
        }
      }
    }
    return newHttpServletRequest;
  }

  /**
   * 根据 Cookie 中的访问令牌判断是否刷新
   *
   * @param accessTokenCookie
   * @return
   */
  private boolean mustRefreshToken(Cookie accessTokenCookie) {
    if (accessTokenCookie == null) {
      return true;
    }
    OAuth2AccessToken token = tokenStore.readAccessToken(accessTokenCookie.getValue());
    if (token.isExpired() || token.getExpiresIn() < REFRESH_WINDOW_SECS) {
      return true;
    }
    return false;
  }

  /**
   * 刷新令牌
   *
   * @param request
   * @param response
   * @param refreshCookie
   * @return
   */
  private HttpServletRequest refreshToken(
      HttpServletRequest request, HttpServletResponse response, Cookie refreshCookie) {
    if (oAuth2CookieHelper.isSessionExpired(refreshCookie)) {
      log.info(MSG_SESSION_EXPIRED_DUE_INACTIVITY);
      oAuth2CookieHelper.clearCookies(request, response);
      return stripTokens(request);
    }

    OAuth2Cookies cookies = this.getCachedCookies(refreshCookie.getValue());
    synchronized (cookies) {
      if (cookies.getAccessTokenCookie() == null) {
        String refreshCookieValue = OAuth2CookieHelper.getRefreshTokenValue(refreshCookie);
        OAuth2AccessToken accessToken = tokenGrantClient.sendRefreshGrant(refreshCookieValue);
        boolean rememberMe = OAuth2CookieHelper.isRememberMe(refreshCookie);
        oAuth2CookieHelper.createCookies(request, accessToken, rememberMe, cookies);
        cookies.addCookiesTo(response);
      } else {
        log.debug(MSG_REUSE_CACHE_REFRESH_TOKEN);
      }
      CookieCollection requestCookies = new CookieCollection(request.getCookies());
      requestCookies.add(cookies.getAccessTokenCookie());
      requestCookies.add(cookies.getRefreshTokenCookie());
      return new CookiesHttpServletRequestWrapper(request, requestCookies.toArray());
    }
  }

  /**
   * 返回不包含令牌的请求
   *
   * @param httpServletRequest
   * @return
   */
  private HttpServletRequest stripTokens(HttpServletRequest httpServletRequest) {
    Cookie[] cookies = oAuth2CookieHelper.stripCookies(httpServletRequest.getCookies());
    return new CookiesHttpServletRequestWrapper(httpServletRequest, cookies);
  }

  /**
   * 获取缓存的 Cookie
   *
   * @param refreshTokenValue
   * @return
   */
  private OAuth2Cookies getCachedCookies(String refreshTokenValue) {
    synchronized (recentlyRefreshedTokenCache) {
      OAuth2Cookies ctx = recentlyRefreshedTokenCache.get(refreshTokenValue);
      if (ctx == null) {
        ctx = new OAuth2Cookies();
        recentlyRefreshedTokenCache.put(refreshTokenValue, ctx);
      }
      return ctx;
    }
  }
}
