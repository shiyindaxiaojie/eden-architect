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
import com.dianping.cat.message.MessageProducer;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.DefaultMessageManager;
import com.dianping.cat.message.internal.DefaultTransaction;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.servlet.CatFilter;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.unidal.helper.Joiners;
import org.ylzl.eden.commons.lang.Chars;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.commons.net.IpConfig;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.spring.framework.web.rest.handler.RestExceptionPostProcessor;
import org.ylzl.eden.spring.integration.cat.extension.CatConstants;
import org.ylzl.eden.spring.integration.cat.integration.web.spi.RestCatExceptionPostProcessor;
import org.ylzl.eden.spring.integration.cat.tracing.TraceContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Http 链路过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class HttpCatFilter extends CatFilter {

	private static final String INTERNAL_ERROR_UNSUPPORTED_MODE = "Internal Error: unsupported mode({})!";

	private final List<Handler> handlers = new ArrayList<>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		handlers.add(CatHandler.ENVIRONMENT);
		handlers.add(CatHandler.ID_SETUP);
		handlers.add(CatHandler.LOG_SPAN);
		handlers.add(CatHandler.LOG_CLIENT_PAYLOAD);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
						 FilterChain chain) throws IOException, ServletException {
		if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}

		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse resp = (HttpServletResponse) servletResponse;
		Context ctx = new Context(req, resp, chain, handlers);
		ctx.handle();
	}

	private static enum CatHandler implements Handler {

		ENVIRONMENT {

			@Override
			public void handle(Context ctx) throws IOException, ServletException {
				HttpServletRequest req = ctx.getRequest();
				boolean top = !Cat.getManager().hasContext();
				ctx.setTop(top);
				if (top) {
					ctx.setMode(detectMode(req));
					ctx.setType(CatConstants.TYPE_URL);
					setTraceMode(req);
				} else {
					ctx.setType(CatConstants.TYPE_URL_FORWARD);
				}

				ctx.handle();
			}

			private int detectMode(HttpServletRequest req) {
				String source = req.getHeader(CatConstants.X_CAT_SOURCE);
				String id = req.getHeader(CatConstants.X_CAT_ID);
				if (CatConstants.SOURCE_CONTAINER.equals(source)) {
					return 2;
				}
				if (id != null && id.length() > 0) {
					return 1;
				}
				return 0;
			}

			protected void setTraceMode(HttpServletRequest req) {
				if (Boolean.parseBoolean(req.getHeader(CatConstants.X_CAT_TRACE_MODE))) {
					Cat.getManager().setTraceMode(true);
				}
			}
		},

		ID_SETUP {

			private String servers;

			@Override
			public void handle(Context ctx) throws IOException, ServletException {
				boolean isTraceMode = Cat.getManager().isTraceMode();
				HttpServletRequest req = ctx.getRequest();
				HttpServletResponse res = ctx.getResponse();
				MessageProducer producer = Cat.getProducer();
				int mode = ctx.getMode();
				switch (mode) {
					case 0:
						ctx.setId(producer.createMessageId());
						break;
					case 1:
						ctx.setRootId(req.getHeader(CatConstants.X_CAT_ROOT_ID));
						ctx.setParentId(req.getHeader(CatConstants.X_CAT_PARENT_ID));
						ctx.setId(req.getHeader(CatConstants.X_CAT_ID));
						break;
					case 2:
						ctx.setRootId(producer.createMessageId());
						ctx.setParentId(ctx.getRootId());
						ctx.setId(producer.createMessageId());
						break;
					default:
						throw new RuntimeException(MessageFormatUtils.format(INTERNAL_ERROR_UNSUPPORTED_MODE, mode));
				}

				if (isTraceMode) {
					MessageTree tree = Cat.getManager().getThreadLocalMessageTree();
					tree.setMessageId(ctx.getId());
					tree.setParentMessageId(ctx.getParentId());
					tree.setRootMessageId(ctx.getRootId());
					res.setHeader(CatConstants.X_CAT_SERVER, getCatServer());

					switch (mode) {
						case 0:
							res.setHeader(CatConstants.X_CAT_ROOT_ID, ctx.getId());
							break;
						case 1:
						case 2:
							res.setHeader(CatConstants.X_CAT_ROOT_ID, ctx.getRootId());
							res.setHeader(CatConstants.X_CAT_PARENT_ID, ctx.getParentId());
							res.setHeader(CatConstants.X_CAT_ID, ctx.getId());
							break;
					}
				}
				ctx.handle();
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
		},

		LOG_CLIENT_PAYLOAD {

			@Override
			public void handle(Context ctx) throws IOException, ServletException {
				HttpServletRequest req = ctx.getRequest();
				String type = ctx.getType();
				if (ctx.isTop()) {
					logRequestClientInfo(req, type);
					logRequestPayload(req, type);
				} else {
					logRequestPayload(req, type);
				}
				ctx.handle();
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
		},

		LOG_SPAN {

			@Override
			public void handle(Context ctx) throws IOException, ServletException {
				HttpServletRequest req = ctx.getRequest();
				Transaction transaction = Cat.newTransaction(ctx.getType(), req.getRequestURI());

				try {
					Cat.Context context = TraceContext.getContext();
					context.addProperty(Cat.Context.ROOT, req.getHeader(CatConstants.X_CAT_ROOT_ID));
					context.addProperty(Cat.Context.PARENT, req.getHeader(CatConstants.X_CAT_PARENT_ID));
					context.addProperty(Cat.Context.CHILD, req.getHeader(CatConstants.X_CAT_ID));
					Cat.logRemoteCallClient(context, Cat.getManager().getDomain());

					MDC.put(TraceContext.TRACE_ID, TraceContext.getTraceId());
					ctx.handle();
					checkRestException(req, ctx.getResponse());
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
