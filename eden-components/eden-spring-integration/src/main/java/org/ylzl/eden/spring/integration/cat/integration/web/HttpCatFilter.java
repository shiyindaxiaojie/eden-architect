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

package org.ylzl.eden.spring.integration.cat.integration.web;

import com.dianping.cat.Cat;
import com.dianping.cat.configuration.NetworkInterfaceManager;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.DefaultMessageManager;
import com.dianping.cat.message.internal.DefaultTransaction;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.servlet.CatFilter;
import com.google.common.collect.Sets;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.unidal.helper.Joiners;
import org.ylzl.eden.commons.env.BrowserUtils;
import org.ylzl.eden.commons.lang.Chars;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.commons.net.IpConfig;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.spring.framework.json.support.JSONHelper;
import org.ylzl.eden.spring.framework.web.rest.handler.RestExceptionPostProcessor;
import org.ylzl.eden.spring.framework.web.util.ServletUtils;
import org.ylzl.eden.spring.integration.cat.CatConstants;
import org.ylzl.eden.spring.integration.cat.integration.web.spi.RestCatExceptionPostProcessor;
import org.ylzl.eden.spring.integration.cat.tracing.TraceContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Http 链路过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class HttpCatFilter extends CatFilter {

	public static final String TRACE_MODE = "traceMode";

	public static final String SUPPORT_OUT_TRACE_ID = "supportOutTraceId";

	public static final String EXCLUDE_URLS = "excludeUrls";

	public static final String INCLUDE_HEADERS = "includeHeaders";

	public static final String INCLUDE_BODY = "includeBody";

	private boolean traceMode = false;

	private boolean supportOutTraceId = false;

	private String servers;

	private Set<String> excludeUrls;

	private Set<String> excludePrefixes;

	private final Set<String> includeHeaders = Sets.newHashSet(
		HttpHeaders.USER_AGENT,
		HttpHeaders.REFERER,
		HttpHeaders.AUTHORIZATION);

	private boolean includeBody = false;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (Boolean.parseBoolean(filterConfig.getInitParameter(TRACE_MODE))) {
			traceMode = true;
		}

		if (Boolean.parseBoolean(filterConfig.getInitParameter(SUPPORT_OUT_TRACE_ID))) {
			supportOutTraceId = true;
		}

		String exclude = filterConfig.getInitParameter(EXCLUDE_URLS);
		if (exclude != null) {
			excludeUrls = new HashSet<>();
			String[] excludeUrls = exclude.split(",");
			for (String s : excludeUrls) {
				int index = s.indexOf("*");
				if (index > 0) {
					if (excludePrefixes == null) {
						excludePrefixes = new HashSet<>();
					}
					excludePrefixes.add(s.substring(0, index));
				} else {
					this.excludeUrls.add(s);
				}
			}
		}

		String includeHeadersParameter = filterConfig.getInitParameter(INCLUDE_HEADERS);
		if (includeHeadersParameter != null) {
			String[] includeHeadersSplit = includeHeadersParameter.split(",");
			this.includeHeaders.addAll(Arrays.asList(includeHeadersSplit));
		}

		if (Boolean.parseBoolean(filterConfig.getInitParameter(INCLUDE_BODY))) {
			includeBody = true;
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String path = req.getRequestURI();
		boolean exclude = this.excludePath(path);
		if (exclude) {
			chain.doFilter(request, response);
			return;
		}

		this.logTransaction(chain, req, resp);
	}

	private void logTransaction(FilterChain chain, HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		Message message = Cat.getManager().getThreadLocalMessageTree().getMessage();
		String type;
		boolean top = message == null;
		if (top) {
			type = CatConstants.TYPE_URL;
			handleIfTraceModeOpen(req);
		} else {
			type = CatConstants.TYPE_URL_FORWARD;
		}

		Transaction transaction = Cat.newTransaction(type, req.getRequestURI());

		try {
			Cat.Context context = initContext(req);
			logPayload(req, top, type);
			logCatMessageId(resp);
			if (supportOutTraceId && StringUtils.isNotBlank(req.getHeader(CatConstants.X_CAT_CHILD_ID))) {
				Cat.logRemoteCallServer(context);
			}
			MDC.put(TraceContext.TRACE_ID, TraceContext.getTraceId());

			chain.doFilter(req, resp);
			checkRestException(req, resp);
			customizeStatus(transaction, req);
		} catch (RuntimeException e) {
			transaction.setStatus(e);
			Cat.logError(e.getMessage(), e);
			throw e;
		} catch (Throwable e) {
			transaction.setStatus(e);
			Cat.logError(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			customizeUri(transaction, req);
			transaction.complete();
		}
	}

	private Cat.Context initContext(HttpServletRequest req) {
		MessageTree tree = Cat.getManager().getThreadLocalMessageTree();

		String parentId;
		if (supportOutTraceId && StringUtils.isNotBlank(req.getHeader(CatConstants.X_CAT_PARENT_ID))) {
			parentId = StringUtils.trimToEmpty(req.getHeader(CatConstants.X_CAT_PARENT_ID));
		} else {
			parentId = Cat.createMessageId();
			tree.setMessageId(parentId);
		}

		Cat.Context context = TraceContext.getContext();

		String rootId;
		if (supportOutTraceId && StringUtils.isNotBlank(req.getHeader(CatConstants.X_CAT_ID))) {
			rootId = StringUtils.trimToEmpty(req.getHeader(CatConstants.X_CAT_ID));
		} else {
			rootId = tree.getRootMessageId();
			if (rootId == null) {
				rootId = parentId;
			}
		}

		String childId = null;
		if (supportOutTraceId && StringUtils.isNotBlank(req.getHeader(CatConstants.X_CAT_CHILD_ID))) {
			childId = StringUtils.trimToEmpty(req.getHeader(CatConstants.X_CAT_CHILD_ID));
		}

		context.addProperty(Cat.Context.ROOT, rootId);
		context.addProperty(Cat.Context.PARENT, parentId);
		context.addProperty(Cat.Context.CHILD, childId);
		return context;
	}

	private void handleIfTraceModeOpen(HttpServletRequest req) {
		if (Boolean.parseBoolean(req.getHeader(CatConstants.X_CAT_TRACE_MODE))) {
			Cat.getManager().setTraceMode(true);
		}
	}

	private void logPayload(HttpServletRequest req, boolean top, String type) {
		try {
			if (top) {
				logRequestClientInfo(req, type);
				logRequestPayload(req, type, true);
			} else {
				logRequestPayload(req, type, false);
			}
		} catch (Exception e) {
			Cat.logError(e.getMessage(), e);
		}
	}

	private void logRequestClientInfo(HttpServletRequest req, String type) {
		String serverIp = req.getServerName();
		Cat.logEvent(CatConstants.TYPE_URL_SERVER, serverIp);

	    String ipForwarded = req.getHeader(CatConstants.X_FORWARDED_FOR);
		String clientIp = ipForwarded == null? req.getRemoteAddr() : ipForwarded;
		Cat.logEvent(CatConstants.TYPE_URL_CLIENT, clientIp);
	}

	private void logRequestPayload(HttpServletRequest req, String type, boolean top) {
		StringBuilder methodInfo = new StringBuilder(256);
		methodInfo.append(req.getScheme().toUpperCase()).append(Strings.SLASH);
		methodInfo.append(req.getMethod()).append(Strings.SPACE).append(req.getRequestURI());
		String queryString = req.getQueryString();
		if (queryString != null) {
			methodInfo.append(Strings.PLACEHOLDER).append(queryString);
		}
		Cat.logEvent(top? CatConstants.TYPE_URL_METHOD : CatConstants.TYPE_URL_FORWARD_METHOD, methodInfo.toString());

		// 请求头埋点
		StringBuilder headerInfo = new StringBuilder(256);
		Enumeration<String> headerNames = req.getHeaderNames();
		int i = 0;
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			if (includeHeaders.contains(headerName)) {
				if (i > 0) {
					headerInfo.append(Strings.AND);
				}
				headerInfo.append(headerName).append(Strings.EQ);
				String headerValue = req.getHeader(headerName);
				if (HttpHeaders.USER_AGENT.equals(headerName)) {
					headerInfo.append(BrowserUtils.parseBrowserWithVersion(headerValue));
				} else {
					headerInfo.append(headerValue);
				}

				i++;
			}
		}
		if (headerInfo.length() > 0) {
			Cat.logEvent(top? CatConstants.TYPE_URL_HEADER : CatConstants.TYPE_URL_FORWARD_HEADER,
				headerInfo.toString());
		}

		// 请求体埋点
		if (includeBody) {
			Cat.logEvent(top? CatConstants.TYPE_URL_BODY : CatConstants.TYPE_URL_FORWARD_BODY,
				JSONHelper.json().toJSONString(ServletUtils.toMap(req)));
		}
	}

	private void logCatMessageId(HttpServletResponse res) {
		if (traceMode) {
			String id = Cat.getCurrentMessageId();
			res.setHeader(CatConstants.X_CAT_ID, id);
			res.setHeader(CatConstants.X_CAT_SERVER, getCatServer());
		}
	}

	private String getCatServer() {
		if (servers == null) {
			DefaultMessageManager manager = (DefaultMessageManager) Cat.getManager();
			servers = Joiners.by(Chars.COMMA).join(manager.getConfigManager().getServers(),
				server -> {
					String ip = server.getIp();
					if (IpConfig.LOCALHOST_IP.equals(ip)) {
						ip = NetworkInterfaceManager.INSTANCE.getLocalHostAddress();
					}
					return ip + Strings.COLON + server.getHttpPort();
				});
		}
		return servers;
	}

	private void customizeStatus(Transaction transaction, HttpServletRequest req) {
		Object catStatus = req.getAttribute(CatConstants.CAT_STATE);
		if (catStatus != null) {
			transaction.setStatus(catStatus.toString());
		} else {
			transaction.setStatus(Message.SUCCESS);
		}
	}

	private void customizeUri(Transaction transaction, HttpServletRequest req) {
		if (transaction instanceof DefaultTransaction) {
			Object catPageType = req.getAttribute(CatConstants.CAT_PAGE_TYPE);
			if (catPageType instanceof String) {
				((DefaultTransaction) transaction).setType(catPageType.toString());
			}

			Object catPageUri = req.getAttribute(CatConstants.CAT_PAGE_URI);
			if (catPageUri instanceof String) {
				((DefaultTransaction) transaction).setName(catPageUri.toString());
			}
		}
	}

	private boolean excludePath(String path) {
		try {
			boolean exclude = excludeUrls != null && excludeUrls.contains(path);
			if (!exclude && excludePrefixes != null) {
				for (String prefix : excludePrefixes) {
					if (path.startsWith(prefix)) {
						exclude = true;
						break;
					}
				}
			}
			return exclude;
		} catch (Exception e) {
			return false;
		}
	}

	private static void checkRestException(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		// 捕获 @RestControllerAdvice 抛出的异常
		RestExceptionPostProcessor processor = ExtensionLoader
			.getExtensionLoader(RestExceptionPostProcessor.class)
			.getExtension(RestCatExceptionPostProcessor.SPI);
		Throwable throwable = processor.getThrowable(req, resp);
		if (throwable != null) {
			throw throwable;
		}
	}
}
