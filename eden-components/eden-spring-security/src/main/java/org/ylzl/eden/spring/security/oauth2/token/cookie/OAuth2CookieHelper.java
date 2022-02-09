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

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.ylzl.eden.spring.framework.web.cookie.CookieCollection;
import org.ylzl.eden.spring.framework.web.cookie.CookieHelper;
import org.ylzl.eden.spring.security.oauth2.constant.OAuth2Constants;
import org.ylzl.eden.spring.security.oauth2.env.OAuth2Properties;
import org.ylzl.eden.spring.security.oauth2.token.TokenProcessor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * OAuth2 Cookie 助手
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class OAuth2CookieHelper {

	private static final List<String> COOKIE_NAMES =
		Arrays.asList(
			OAuth2Constants.ACCESS_TOKEN,
			OAuth2Constants.REFRESH_TOKEN,
			OAuth2Constants.SESSION_TOKEN);

	private static final long REFRESH_TOKEN_EXPIRATION_WINDOW_SECS = 3L;

	private static final String MSG_CREATE_COOKIE_IN_DOMAIN =
		"creating OAuth2 cookies for domain: {}";

	private static final String MSG_CREATE_ACCESS_TOKEN_COOKIE =
		"created OAuth2 access token cookie '{}'";

	private static final String MSG_CREATE_REFRESH_TOKEN_COOKIE =
		"created OAuth2 refresh token cookie '{}', age: {}";

	private static final String MSG_REMEMBER_REFRESH_TOKEN_COOKIE =
		"refresh OAuth2 token valid for another {} secs";

	private static final String MSG_SESSION_DURATION =
		"OAuth2 session duration {} secs, will timeout at {}";

	private static final String MSG_CLEAR_COOKIE = "Clear OAuth2 Cookie：{}";

	private final TokenProcessor tokenProcessor;

	private final OAuth2Properties.Authorization oAuth2Properties;

	public OAuth2CookieHelper(TokenProcessor tokenProcessor, OAuth2Properties oAuth2Properties) {
		this.tokenProcessor = tokenProcessor;
		this.oAuth2Properties = oAuth2Properties.getAuthorization();
	}

	/**
	 * 获取访问令牌的 Cookie
	 *
	 * @param request
	 * @return
	 */
	public static Cookie getAccessTokenCookie(HttpServletRequest request) {
		return CookieHelper.get(request, OAuth2Constants.ACCESS_TOKEN);
	}

	/**
	 * 获取刷新令牌的 Cookie
	 *
	 * @param request
	 * @return
	 */
	public static Cookie getRefreshTokenCookie(HttpServletRequest request) {
		Cookie cookie = CookieHelper.get(request, OAuth2Constants.REFRESH_TOKEN);
		if (cookie == null) {
			cookie = CookieHelper.get(request, OAuth2Constants.SESSION_TOKEN);
		}
		return cookie;
	}

	/**
	 * 根据刷新令牌的 Cookie 判断记住我
	 *
	 * @param refreshTokenCookie
	 * @return
	 */
	public static boolean isRememberMe(Cookie refreshTokenCookie) {
		return refreshTokenCookie.getName().equals(OAuth2Constants.REFRESH_TOKEN);
	}

	/**
	 * 从刷新令牌的 Cookie 获取值
	 *
	 * @param refreshCookie
	 * @return
	 */
	public static String getRefreshTokenValue(Cookie refreshCookie) {
		String value = refreshCookie.getValue();
		int i = value.indexOf('|');
		if (i > 0) {
			return value.substring(i + 1);
		}
		return value;
	}

	/**
	 * 获取 Cookie 的域
	 *
	 * @param request
	 * @return
	 */
	public String getCookieDomain(HttpServletRequest request) {
		String domain = oAuth2Properties.getCookieDomain();
		if (domain != null) {
			return domain;
		}
		return CookieHelper.getDomain(request);
	}

	/**
	 * 跳过访问令牌相关的 Cookie
	 *
	 * @param cookies
	 * @return
	 */
	public Cookie[] stripCookies(Cookie[] cookies) {
		CookieCollection cc = new CookieCollection(cookies);
		if (cc.removeAll(COOKIE_NAMES)) {
			return cc.toArray();
		}
		return cookies;
	}

	/**
	 * 清空访问令牌相关的 Cookie
	 *
	 * @param request
	 * @param response
	 */
	public void clearCookies(HttpServletRequest request, HttpServletResponse response) {
		log.debug(MSG_CLEAR_COOKIE, COOKIE_NAMES);
		String domain = getCookieDomain(request);
		for (String cookieName : COOKIE_NAMES) {
			CookieHelper.clear(request, response, cookieName, domain);
		}
	}

	/**
	 * 创建访问令牌和刷新令牌的 Cookie
	 *
	 * @param request
	 * @param accessToken
	 * @param rememberMe
	 * @param oAuth2Cookies
	 */
	public void createCookies(
		HttpServletRequest request,
		OAuth2AccessToken accessToken,
		boolean rememberMe,
		OAuth2Cookies oAuth2Cookies) {
		String domain = getCookieDomain(request);
		log.debug(MSG_CREATE_COOKIE_IN_DOMAIN, domain);

		Cookie accessTokenCookie =
			CookieHelper.create(OAuth2Constants.ACCESS_TOKEN, accessToken.getValue());
		CookieHelper.set(accessTokenCookie, request.isSecure(), domain);
		log.debug(MSG_CREATE_ACCESS_TOKEN_COOKIE, accessTokenCookie.getName());

		Cookie refreshTokenCookie = createRefreshTokenCookie(accessToken.getRefreshToken(), rememberMe);
		CookieHelper.set(refreshTokenCookie, request.isSecure(), domain);
		log.debug(
			MSG_CREATE_REFRESH_TOKEN_COOKIE,
			refreshTokenCookie.getName(),
			refreshTokenCookie.getMaxAge());

		oAuth2Cookies.setCookies(accessTokenCookie, refreshTokenCookie);
	}

	/**
	 * 判断刷新令牌是否会话失效
	 *
	 * @param refreshCookie
	 * @return
	 */
	public boolean isSessionExpired(Cookie refreshCookie) {
		if (isRememberMe(refreshCookie)) {
			return false;
		}
		int validity = oAuth2Properties.getSessionTimeoutSeconds();
		if (validity < 0) {
			return false;
		}
		Integer iat = tokenProcessor.getIat(refreshCookie.getValue());
		if (iat == null) {
			return false;
		}
		int now = (int) (System.currentTimeMillis() / 1000L);
		int sessionDuration = now - iat;
		log.debug(MSG_SESSION_DURATION, sessionDuration, validity);
		return sessionDuration > validity;
	}

	/**
	 * 创建刷新令牌的 Cookie
	 *
	 * @param refreshToken
	 * @param rememberMe
	 * @return
	 */
	private Cookie createRefreshTokenCookie(OAuth2RefreshToken refreshToken, boolean rememberMe) {
		int maxAge = -1;
		String name = OAuth2Constants.SESSION_TOKEN;
		String value = refreshToken.getValue();
		if (rememberMe) {
			name = OAuth2Constants.REFRESH_TOKEN;
			Integer exp = tokenProcessor.getExp(value);
			if (exp != null) {
				int now = (int) (System.currentTimeMillis() / 1000L);
				maxAge = exp - now;
				log.debug(MSG_REMEMBER_REFRESH_TOKEN_COOKIE, maxAge);
				maxAge -= REFRESH_TOKEN_EXPIRATION_WINDOW_SECS;
			}
		}
		Cookie refreshTokenCookie = CookieHelper.create(name, value);
		refreshTokenCookie.setMaxAge(maxAge);
		return refreshTokenCookie;
	}
}
