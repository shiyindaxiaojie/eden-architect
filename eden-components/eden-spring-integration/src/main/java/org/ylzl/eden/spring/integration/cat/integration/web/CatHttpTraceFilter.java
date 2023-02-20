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
import com.dianping.cat.message.Transaction;
import com.dianping.cat.servlet.CatFilter;
import org.slf4j.MDC;
import org.ylzl.eden.extension.ExtensionLoader;
import org.ylzl.eden.spring.framework.web.rest.handler.RestExceptionPostProcessor;
import org.ylzl.eden.spring.integration.cat.extension.CatConstants;
import org.ylzl.eden.spring.integration.cat.integration.web.spi.CatRestExceptionPostProcessor;
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
public class CatHttpTraceFilter extends CatFilter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
						 FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		Transaction t = Cat.newTransaction(CatConstants.TYPE_URL, request.getRequestURI());
		try {
			Cat.Context context = TraceContext.getContext();
			context.addProperty(Cat.Context.ROOT, request.getHeader(CatConstants.HTTP_HEADER_ROOT_MESSAGE_ID));
			context.addProperty(Cat.Context.PARENT, request.getHeader(CatConstants.HTTP_HEADER_PARENT_MESSAGE_ID));
			context.addProperty(Cat.Context.CHILD, request.getHeader(CatConstants.HTTP_HEADER_CHILD_MESSAGE_ID));
			Cat.logEvent(CatConstants.TYPE_URL, request.getRequestURI());
			Cat.logRemoteCallClient(context, Cat.getManager().getDomain());

			MDC.put(TraceContext.TRACE_ID, TraceContext.getTraceId());
			filterChain.doFilter(servletRequest, servletResponse);

			this.checkRestException(request, response);
			t.setStatus(Transaction.SUCCESS);
		} catch (ServletException | IOException e) {
			t.setStatus(e);
			Cat.logError(e);
			throw e;
		} catch (Throwable e) {
			t.setStatus(e);
			Cat.logError(e);
			throw new RuntimeException(e);
		} finally {
			t.complete();
			TraceContext.remove();
		}
	}

	private void checkRestException(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		RestExceptionPostProcessor processor = ExtensionLoader
			.getExtensionLoader(RestExceptionPostProcessor.class)
			.getExtension(CatRestExceptionPostProcessor.SPI);
		Throwable throwable = processor.getThrowable(request, response);
		if (throwable != null) {
			throw throwable;
		}
	}

}
