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

package org.ylzl.eden.spring.integration.cat.integration.rest;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.ylzl.eden.commons.env.Charsets;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.commons.net.IpConfigUtils;
import org.ylzl.eden.spring.integration.cat.CatConstants;
import org.ylzl.eden.spring.integration.cat.tracing.TraceContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Set;

/**
 * RestTemplate 链路过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class RestTemplateCatInterceptor implements ClientHttpRequestInterceptor {

	private static final String BIZ_ERROR = "BIZ_ERROR";
	private static final String TIMEOUT = "TIMEOUT";

	private final Set<String> includeHeaders = Sets.newHashSet(
		HttpHeaders.USER_AGENT,
		HttpHeaders.REFERER,
		HttpHeaders.AUTHORIZATION);

	private boolean includeBody = false;

	@Override
	public @NotNull ClientHttpResponse intercept(HttpRequest req, byte[] body,
												 ClientHttpRequestExecution execution) throws IOException {
		ClientHttpResponse response;
		String url = getUrl(req);
		Transaction transaction = Cat.newTransaction(CatConstants.TYPE_URL, url);
		try {
			Cat.Context context = TraceContext.getContext();
			this.logRequestClientInfo(req);
			this.logRequestPayload(req, body);
			Cat.logRemoteCallClient(context, Cat.getManager().getDomain());
			MDC.put(TraceContext.TRACE_ID, TraceContext.getTraceId());
			this.setHeader(req);
			response = execution.execute(req, body);
			HttpStatus httpStatus = response.getStatusCode();
			String status = httpStatus.is2xxSuccessful()? Message.SUCCESS : httpStatus.name();
			if (httpStatus.is3xxRedirection()) {
				Cat.logEvent(CatConstants.TYPE_URL, CatConstants.TYPE_URL_REDIRECTION_ERROR, status, url);
			} else if (httpStatus.is4xxClientError()) {
				Cat.logEvent(CatConstants.TYPE_URL, CatConstants.TYPE_URL_CLIENT_ERROR, status, url);
			} else if (httpStatus.is5xxServerError()) {
				Cat.logEvent(CatConstants.TYPE_URL, CatConstants.TYPE_URL_SERVER_ERROR, status, url);
			}
			transaction.setStatus(status);
			return response;
		} catch (Exception e) {
			if (e instanceof SocketTimeoutException) {
				Cat.logEvent(CatConstants.TYPE_URL, CatConstants.TYPE_URL_TIMEOUT_ERROR, TIMEOUT, url);
			} else {
				Cat.logEvent(CatConstants.TYPE_URL, CatConstants.TYPE_URL_BIZ_ERROR, BIZ_ERROR, url);
			}
			transaction.setStatus(e);
			Cat.logError(e);
			throw e;
		} finally {
			transaction.complete();
		}
	}

	public void setIncludeHeaders(String includeHeaders) {
		if (includeHeaders != null) {
			String[] includeHeadersSplit = includeHeaders.split(",");
			this.includeHeaders.addAll(Arrays.asList(includeHeadersSplit));
		}
	}

	public void setIncludeBody(boolean includeBody) {
		this.includeBody = includeBody;
	}

	private void logRequestClientInfo(HttpRequest req) {
		StringBuilder serverInfo = new StringBuilder(1024);
		String ipForwarded = req.getHeaders().getFirst(CatConstants.X_FORWARDED_FOR);
		if (ipForwarded == null) {
			serverInfo.append("clientIp=").append(IpConfigUtils.getIpAddress());
		} else {
			serverInfo.append("clientIpForwarded=").append(ipForwarded);
		}
		serverInfo.append("&serverIp=").append(req.getURI().getHost());
		Cat.logEvent(CatConstants.TYPE_URL, CatConstants.TYPE_URL_SERVER, Message.SUCCESS, serverInfo.toString());
	}

	@SneakyThrows(UnsupportedEncodingException.class)
	private void logRequestPayload(HttpRequest req, byte[] body) {
		StringBuilder methodInfo = new StringBuilder(256);
		methodInfo.append(req.getURI().getScheme().toUpperCase()).append(Strings.SLASH);
		methodInfo.append(req.getMethod()).append(Strings.SPACE).append(req.getURI().getPath());
		String queryString = req.getURI().getQuery();
		if (queryString != null) {
			methodInfo.append(Strings.PLACEHOLDER).append(queryString);
		}
		Cat.logEvent(CatConstants.TYPE_URL, CatConstants.TYPE_URL_METHOD, Message.SUCCESS, methodInfo.toString());

		// 请求头埋点
		StringBuilder headerInfo = new StringBuilder(256);
		Set<String> headerNames = req.getHeaders().keySet();
		int i = 0;
		for (String headerName : headerNames) {
			if (includeHeaders.contains(headerName)) {
				if (i > 0) {
					headerInfo.append(Strings.AND);
				}
				headerInfo.append(headerName).append(Strings.EQ).append(req.getHeaders().get(headerName));
				i++;
			}
		}
		if (headerInfo.length() > 0) {
			Cat.logEvent(CatConstants.TYPE_URL, CatConstants.TYPE_URL_HEADER, Message.SUCCESS,
				headerInfo.toString());
		}

		// 请求体埋点
		if (includeBody) {
			Cat.logEvent(CatConstants.TYPE_URL, CatConstants.TYPE_URL_BODY,
				Message.SUCCESS, new String(body, Charsets.UTF_8_NAME));
		}
	}

	private void setHeader(HttpRequest req) {
		req.getHeaders().add(CatConstants.X_CAT_ID, TraceContext.getContext().getProperty(Cat.Context.ROOT));
		req.getHeaders().add(CatConstants.X_CAT_CHILD_ID, TraceContext.getContext().getProperty(Cat.Context.CHILD));
		req.getHeaders().add(CatConstants.X_CAT_PARENT_ID, TraceContext.getContext().getProperty(Cat.Context.PARENT));
	}

	private String getUrl(HttpRequest req) {
		return req.getURI().getScheme() + "://"
			+ req.getURI().getHost() + ":" + req.getURI().getPort()
			+ req.getURI().getPath()
			+ (StringUtils.isNotBlank(req.getURI().getQuery())? ("?" + req.getURI().getQuery()) : Strings.EMPTY);
	}
}
