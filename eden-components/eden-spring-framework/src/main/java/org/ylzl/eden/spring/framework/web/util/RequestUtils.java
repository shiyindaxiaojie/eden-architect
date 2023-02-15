/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.framework.web.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.ylzl.eden.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Request 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class RequestUtils {

	public static ServletRequestAttributes getRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}

	public static HttpServletRequest getRequest() {
		return getRequestAttributes().getRequest();
	}

	public static HttpServletResponse getResponse() {
		return getRequestAttributes().getResponse();
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static String getRemoteUser() {
		HttpServletRequest request = getRequest();
		return getRemoteUser(request);
	}

	public static String getRemoteUser(HttpServletRequest request) {
		return StringUtils.trimToEmpty(request.getRemoteUser());
	}

	public static String getRequestURI() {
		return getRequestURI(getRequest());
	}

	public static String getRequestURI(HttpServletRequest request) {
		return request.getRequestURI();
	}

	public static String getRequestURL() {
		return getRequestURL(getRequest());
	}

	public static String getRequestURL(HttpServletRequest request) {
		return request.getRequestURL().toString();
	}

	public static String getContextPath() {
		HttpServletRequest request = getRequest();
		return getContextPath(request);
	}

	public static String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}

	public static String getQueryString() {
		HttpServletRequest request = getRequest();
		return getQueryString(request);
	}

	public static String getQueryString(HttpServletRequest request) {
		return request.getQueryString();
	}

	public static String getRequestPath() {
		HttpServletRequest request = getRequest();
		return getRequestPath(request);
	}

	public static String getRequestPath(HttpServletRequest request) {
		String queryString = request.getQueryString();
		String requestURI = request.getRequestURI();
		if (StringUtils.isNotEmpty(queryString)) {
			requestURI += "?" + queryString;
		}
		int index = requestURI.indexOf("&");
		if (index > -1) {
			requestURI = requestURI.substring(0, index);
		}
		return requestURI.substring(request.getContextPath().length() + 1);
	}

	public static boolean isAjaxRequest() {
		HttpServletRequest request = getRequest();
		return isAjaxRequest(request);
	}

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String accept = request.getHeader("accept");
		if (accept != null && accept.contains("application/json")) {
			return true;
		}
		String xRequestedWith = request.getHeader("X-Requested-With");
		return xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest");
	}
}
