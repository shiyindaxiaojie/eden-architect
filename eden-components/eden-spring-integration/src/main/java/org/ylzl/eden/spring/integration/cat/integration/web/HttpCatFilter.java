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
import com.dianping.cat.servlet.CatFilter;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.unidal.helper.Joiners;
import org.ylzl.eden.commons.lang.Chars;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.commons.net.IpConfig;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.spring.framework.web.rest.handler.RestExceptionPostProcessor;
import org.ylzl.eden.spring.integration.cat.CatConstants;
import org.ylzl.eden.spring.integration.cat.integration.web.spi.RestCatExceptionPostProcessor;
import org.ylzl.eden.spring.integration.cat.tracing.TraceContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Http 链路过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class HttpCatFilter extends CatFilter {

	private static final AtomicBoolean TRACE_MODE = new AtomicBoolean(false);

	private static final AtomicBoolean SUPPORT_OUT_TRACE_ID = new AtomicBoolean(false);

	private String servers;

	private Set<String> excludeUrls;

	private Set<String> excludePrefixes;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String exclude = filterConfig.getInitParameter("exclude");
		if (exclude != null) {
			excludeUrls = new HashSet<>();
			String[] excludeUrls = exclude.split(";");
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

	public static void setTraceMode(boolean traceMode) {
		TRACE_MODE.set(traceMode);
	}

	public static void setSupportOutTraceId(boolean traceId) {
		SUPPORT_OUT_TRACE_ID.set(traceId);
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
			handleIfTraceIdAvailable(req);
			logPayload(req, top, type);
			logCatMessageId(resp);

			Cat.Context context = TraceContext.getContext();
			Cat.logRemoteCallClient(context, Cat.getManager().getDomain());
			MDC.put(TraceContext.TRACE_ID, TraceContext.getTraceId());

			chain.doFilter(req, resp);
			checkRestException(req, resp);
			customizeStatus(transaction, req);
		} catch (ServletException | IOException e) {
			transaction.setStatus(e);
			Cat.logError(e);
			throw e;
		} catch (Throwable e) {
			transaction.setStatus(e);
			Cat.logError(e);
			throw new RuntimeException(e);
		} finally {
			customizeUri(transaction, req);
			transaction.complete();
		}
	}

	private void handleIfTraceModeOpen(HttpServletRequest req) {
		if (Boolean.parseBoolean(req.getHeader(CatConstants.X_CAT_TRACE_MODE))) {
			Cat.getManager().setTraceMode(true);
		}
	}

	private void handleIfTraceIdAvailable(HttpServletRequest req) {
		String traceId = req.getHeader(CatConstants.X_CAT_ID);
		if (SUPPORT_OUT_TRACE_ID.get() && StringUtils.isNotBlank(traceId)) {
			Cat.getManager().getThreadLocalMessageTree().setMessageId(traceId);
		}
	}

	private void logPayload(HttpServletRequest req, boolean top, String type) {
		try {
			if (top) {
				logRequestClientInfo(req, type);
				logRequestPayload(req, type);
			} else {
				logRequestPayload(req, type);
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
	}

	private void logRequestClientInfo(HttpServletRequest req, String type) {
		StringBuilder sb = new StringBuilder(1024);
		String ipForwarded = req.getHeader(CatConstants.X_FORWARDED_FOR);
		if (ipForwarded == null) {
			sb.append("clientIp=").append(req.getRemoteAddr());
		} else {
			sb.append("clientIpForwarded=").append(ipForwarded);
		}
		sb.append("&serverIp=").append(req.getServerName());
		sb.append("&referer=").append(req.getHeader(HttpHeaders.REFERER));
		sb.append("&userAgent=").append(req.getHeader(HttpHeaders.USER_AGENT));
		Cat.logEvent(type, CatConstants.TYPE_URL_SERVER, Message.SUCCESS, sb.toString());
	}

	private void logRequestPayload(HttpServletRequest req, String type) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(req.getScheme().toUpperCase()).append(Strings.SLASH);
		sb.append(req.getMethod()).append(Strings.SPACE).append(req.getRequestURI());
		String queryString = req.getQueryString();
		if (queryString != null) {
			sb.append(Strings.PLACEHOLDER).append(queryString);
		}
		Cat.logEvent(type, CatConstants.TYPE_URL_METHOD, Message.SUCCESS, sb.toString());
	}

	private void logCatMessageId(HttpServletResponse res) {
		if (TRACE_MODE.get()) {
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
