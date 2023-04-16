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
import org.jetbrains.annotations.PropertyKey;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.ylzl.eden.commons.io.FileUtils;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.framework.bootstrap.constant.Globals;
import org.ylzl.eden.spring.framework.error.ErrorCodeLoader;
import org.ylzl.eden.spring.framework.json.support.JSONHelper;
import org.ylzl.eden.spring.framework.web.extension.ResponseBuilder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Servlet 工具类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public final class ServletUtils {

	public static final String ACCEPT_RANGES = "bytes";

	public static final String CONTENT_DISPOSITION_ATTACH = "attachment;filename={0}";

	/** Spring 已标记弃用，但用户不升级 Chrome 是无法解决问题的 */
	public static final String APPLICATION_JSON_UTF8_VALUE = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";

	public static Map<String, String> toMap(ServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		Map<String, String> returnMap = new HashMap<String, String>();
		for (Entry<String, String[]> entry : parameterMap.entrySet()) {
			Object valueObj = entry.getValue();
			if (ObjectUtils.isEmpty(valueObj)) {
				returnMap.put(entry.getKey(), Strings.EMPTY);
			} else {
				String[] values = (String[]) valueObj;
				StringBuilder sb = new StringBuilder();
				for (String val : values) {
					sb.append(val).append(Strings.DOT);
				}
				if (sb.indexOf(Strings.DOT) >= 0) {
					sb.delete(sb.length() - 1, sb.length());
				}
				returnMap.put(entry.getKey(), sb.toString());
			}
		}
		return returnMap;
	}

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

	public static String getRemoteAddr() {
		HttpServletRequest request = getRequest();
		return getRemoteAddr(request);
	}

	public static String getRemoteAddr(HttpServletRequest request) {
		return StringUtils.trimToEmpty(request.getRemoteAddr());
	}

	public static String getRemoteHost() {
		HttpServletRequest request = getRequest();
		return getRemoteHost(request);
	}

	public static String getRemoteHost(HttpServletRequest request) {
		return StringUtils.trimToEmpty(request.getRemoteHost());
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

	public static String getRequestParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		return parameterMap.isEmpty() ? Strings.EMPTY : parameterMap.entrySet().stream()
			.map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
			.collect(Collectors.joining(", "));
	}

	public static String getRequestHeaders(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> headerNames = request.getHeaderNames();
		int i = 0;
		while (headerNames.hasMoreElements()) {
			if (i > 0) {
				sb.append(", ");
			}
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			sb.append(headerName).append("=").append(headerValue);
			i++;
		}
		return sb.toString();
	}

	public static String getRequestBody(HttpServletRequest request) {
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				return reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public static String getResponseHeaders(HttpServletResponse response) {
		return response.getHeaderNames().stream()
			.map(name -> name + "=" + response.getHeader(name))
			.collect(Collectors.joining(", "));
	}

	public static String getResponseBody(HttpServletResponse response) {
		String contentType = response.getContentType();
		if (contentType != null && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
			return response.toString();
		}
		return Strings.EMPTY;
	}

	public static boolean isAjaxRequest() {
		HttpServletRequest request = getRequest();
		return isAjaxRequest(request);
	}

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String accept = request.getHeader("accept");
		if (accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE)) {
			return true;
		}
		String xRequestedWith = request.getHeader("X-Requested-With");
		return xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest");
	}

	/**
	 * 封装响应
	 *
	 * @param response
	 * @param statueCode
	 * @throws IOException
	 */
	public static void wrap(HttpServletResponse response, int statueCode) throws IOException {
		response.setStatus(statueCode);
		response.setContentType(APPLICATION_JSON_UTF8_VALUE);
		PrintWriter out = response.getWriter();
		String result = JSONHelper.json().toJSONString(ResponseBuilder.builder().buildSuccess());
		out.write(result);
	}

	/**
	 * 封装响应
	 *
	 * @param response
	 * @param statueCode
	 * @throws IOException
	 */
	public static void wrap(HttpServletResponse response, int statueCode,
							@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
							String errMessage, Object... params) throws IOException {
		response.setStatus(statueCode);
		response.setContentType(APPLICATION_JSON_UTF8_VALUE);
		PrintWriter out = response.getWriter();
		String result = JSONHelper.json().toJSONString(ResponseBuilder.builder().buildFailure(errCode, errMessage, params));
		out.write(result);
	}

	/**
	 * 封装响应
	 *
	 * @param maybeResponse
	 * @param <X>
	 * @return
	 */
	public static <X> ResponseEntity<X> wrapOrNotFound(X maybeResponse) {
		return wrapOrNotFound(maybeResponse, null);
	}

	/**
	 * 封装响应
	 *
	 * @param maybeResponse
	 * @param header
	 * @param <X>
	 * @return
	 */
	public static <X> ResponseEntity<X> wrapOrNotFound(X maybeResponse, HttpHeaders header) {
		if (maybeResponse == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok().headers(header).body(maybeResponse);
	}

	/**
	 * 请求头 <br>
	 * Range：bytes=0-100 <br>
	 *
	 * <p>响应头 <br>
	 * Content-Range：bytes 0-100/1234 <br>
	 * Content-Length：101 <br>
	 *
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, File file)
		throws IOException {
		String fileName = file.getName();
		response.setHeader(
			HttpHeaders.CONTENT_DISPOSITION,
			MessageFormat.format(
				CONTENT_DISPOSITION_ATTACH,
				URLEncoder.encode(fileName, Globals.DEFAULT_ENCODING)));

		String contentType = request.getServletContext().getMimeType(fileName);
		response.setContentType(contentType);
		response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

		long fileLength = file.length();
		if (StringUtils.isNotBlank(resolveRange(request))) {
			long startByte = resolveStartBytesFromRange(request); // 开始下载位置
			long endByte = resolveEndBytesFromRange(request); // 结束下载位置
			if (endByte == 0) {
				endByte = fileLength - 1;
			}
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			long contentLength = endByte - startByte + 1;
			response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
			response.setHeader(HttpHeaders.ACCEPT_RANGES, ACCEPT_RANGES);
			response.setHeader(
				HttpHeaders.CONTENT_RANGE,
				StringUtils.join(
					ACCEPT_RANGES,
					Strings.SPACE,
					startByte,
					Strings.MINUS,
					endByte,
					Strings.SLASH,
					fileLength));

			FileUtils.seek(file, response.getOutputStream(), startByte, endByte);
			response.flushBuffer();
		}
		response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength));
		FileUtils.allocateDirect(file.getAbsolutePath(), response.getOutputStream());
	}

	public static String resolveRange(HttpServletRequest request) {
		String range = request.getHeader(HttpHeaders.RANGE);
		if (range != null
			&& range.contains(StringUtils.join(ACCEPT_RANGES, Strings.EQ))
			&& range.contains(Strings.MINUS)) {
			return range;
		}
		return null;
	}

	public static long resolveStartBytesFromRange(HttpServletRequest request)
		throws NumberFormatException {
		String range = resolveRange(request);
		if (StringUtils.isBlank(range)) {
			return -1;
		}
		long startByte = 0;
		String[] ranges =
			range
				.substring(range.lastIndexOf(Strings.EQ) + 1)
				.trim()
				.split(Strings.MINUS);
		if (ranges.length == 1) {
			if (range.endsWith(Strings.MINUS)) { // bytes=1234-
				startByte = Long.parseLong(ranges[0]);
			}
		} else if (ranges.length == 2) { // bytes=1234-9999
			startByte = Long.parseLong(ranges[0]);
		}
		return startByte;
	}

	public static long resolveEndBytesFromRange(HttpServletRequest request)
		throws NumberFormatException {
		String range = resolveRange(request);
		if (StringUtils.isBlank(range)) {
			return -1;
		}
		long endByte = 0;
		String[] ranges =
			range
				.substring(range.lastIndexOf(Strings.EQ) + 1)
				.trim()
				.split(Strings.MINUS);
		if (ranges.length == 1) {
			if (range.startsWith(Strings.MINUS)) { // bytes=-1234
				endByte = Long.parseLong(ranges[0]);
			}
		} else if (ranges.length == 2) { // bytes=1234-9999
			endByte = Long.parseLong(ranges[1]);
		}
		return endByte;
	}
}
