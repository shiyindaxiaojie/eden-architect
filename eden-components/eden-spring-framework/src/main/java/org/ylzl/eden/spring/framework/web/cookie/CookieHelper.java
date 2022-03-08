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

package org.ylzl.eden.spring.framework.web.cookie;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie 助手
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class CookieHelper {

	private static final PublicSuffixMatcher suffixMatcher = PublicSuffixMatcherLoader.getDefault();

	public static Cookie create(String cookieName, String cookieValue) {
		return new Cookie(cookieName, cookieValue);
	}

	public static Cookie get(HttpServletRequest request, String cookieName) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(cookieName)) {
					String value = cookie.getValue();
					if (StringUtils.isNotBlank(value)) {
						return cookie;
					}
				}
			}
		}
		return null;
	}

	public static void clear(
		HttpServletRequest request, HttpServletResponse response, String cookieName, String domain) {
		Cookie cookie = new Cookie(cookieName, StringConstants.EMPTY);
		set(cookie, request.isSecure(), domain);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	public static void set(Cookie cookie, boolean isSecure, String domain) {
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setSecure(isSecure);
		if (domain != null) {
			cookie.setDomain(domain);
		}
	}

	public static String getDomain(HttpServletRequest request) {
		String domain = request.getServerName().toLowerCase();
		if (domain.startsWith("www.")) {
			domain = domain.substring(4);
		}
		if (!InetAddressUtils.isIPv4Address(domain) && !InetAddressUtils.isIPv6Address(domain)) {
			String suffix = suffixMatcher.getDomainRoot(domain);
			if (suffix != null && !suffix.equals(domain)) {
				return StringConstants.DOT + suffix;
			}
		}
		return null;
	}
}
