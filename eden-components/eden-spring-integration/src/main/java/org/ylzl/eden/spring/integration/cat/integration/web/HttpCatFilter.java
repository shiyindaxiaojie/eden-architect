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
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.servlet.CatFilter;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.spring.framework.web.rest.handler.RestExceptionPostProcessor;
import org.ylzl.eden.spring.integration.cat.extension.CatConstants;
import org.ylzl.eden.spring.integration.cat.integration.web.spi.RestCatExceptionPostProcessor;
import org.ylzl.eden.spring.integration.cat.tracing.TraceContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Http 链路过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class HttpCatFilter extends CatFilter {

	public static final String X_FORWARDED_FOR = "x-forwarded-for";

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
						 FilterChain chain) throws IOException, ServletException {
		if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		this.logTransaction(request, response, chain);
	}

	private void logTransaction(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		boolean top = Cat.getManager().getThreadLocalMessageTree().getMessage() == null;
		String type;
		if (top) {
			setTraceMode(request);
			type = CatConstants.TYPE_URL;
		} else {
			type = CatConstants.TYPE_URL_FORWARD;
		}

		Transaction transaction = Cat.newTransaction(type, request.getRequestURI());
		Cat.Context context = TraceContext.getContext();
		context.addProperty(Cat.Context.ROOT, request.getHeader(CatConstants.HTTP_HEADER_ROOT_MESSAGE_ID));
		context.addProperty(Cat.Context.PARENT, request.getHeader(CatConstants.HTTP_HEADER_PARENT_MESSAGE_ID));
		context.addProperty(Cat.Context.CHILD, request.getHeader(CatConstants.HTTP_HEADER_CHILD_MESSAGE_ID));
		MDC.put(TraceContext.TRACE_ID, TraceContext.getTraceId());
		try {
			Cat.logEvent(CatConstants.TYPE_URL, request.getRequestURI());
			if (top) {
				this.logEventUrlServer(request, type);
			}
			this.logEventUrlMethod(request, type);

			Cat.logRemoteCallClient(context, Cat.getManager().getDomain());

			chain.doFilter(request, response);
			this.checkRestException(request, response);
			transaction.setStatus(Transaction.SUCCESS);
		} catch (ServletException | IOException e) {
			transaction.setStatus(e);
			Cat.logError(e);
			throw e;
		} catch (Throwable e) {
			transaction.setStatus(e);
			Cat.logError(e);
			throw new RuntimeException(e);
		} finally {
			transaction.complete();
			TraceContext.remove();
		}
	}

	private void setTraceMode(HttpServletRequest request) {
		if (Boolean.parseBoolean(request.getHeader(CatConstants.TRACE_MODE))) {
			Cat.getManager().setTraceMode(true);
		}
	}

	private void logEventUrlServer(HttpServletRequest req, String type) {
		StringBuilder sb = new StringBuilder(1024);
		String ip;
		String ipForwarded = req.getHeader(X_FORWARDED_FOR);
		if (ipForwarded == null) {
			ip = req.getRemoteAddr();
		} else {
			ip = ipForwarded;
		}
		sb.append("clientIp=").append(ip);
		sb.append("&serverIp=").append(req.getServerName());
		sb.append("&referer=").append(req.getHeader(HttpHeaders.REFERER));
		sb.append("&userAgent=").append(req.getHeader(HttpHeaders.USER_AGENT));
		Cat.logEvent(type, CatConstants.TYPE_URL_SERVER, Message.SUCCESS, sb.toString());
	}

	private void logEventUrlMethod(HttpServletRequest req, String type) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(req.getScheme().toUpperCase()).append(Strings.SLASH);
		sb.append(req.getMethod()).append(Strings.SPACE).append(req.getRequestURI());
		String queryString = req.getQueryString();
		if (queryString != null) {
			sb.append(Strings.PLACEHOLDER).append(queryString);
		}
		Cat.logEvent(type, CatConstants.TYPE_URL_METHOD, Message.SUCCESS, sb.toString());
	}

	private void checkRestException(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// 捕获 @RestControllerAdvice 抛出的异常
		RestExceptionPostProcessor processor = ExtensionLoader
			.getExtensionLoader(RestExceptionPostProcessor.class)
			.getExtension(RestCatExceptionPostProcessor.SPI);
		Throwable throwable = processor.getThrowable(request, response);
		if (throwable != null) {
			throw throwable;
		}
	}
}
