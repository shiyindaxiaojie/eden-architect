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
import com.dianping.cat.message.Transaction;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.ylzl.eden.spring.integration.cat.extension.CatConstants;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * RestTemplate 链路过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CatRestTemplateTraceInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public @NotNull ClientHttpResponse intercept(HttpRequest req, byte[] body,
												 ClientHttpRequestExecution execution) throws IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		Transaction t = Cat.newTransaction(CatConstants.TYPE_URL, request.getRequestURI());
		try {
			return execution.execute(req, body);
		} catch (Exception e) {
			t.setStatus(e);
			Cat.logError(e);
			throw e;
		} finally {
			t.complete();
		}
	}
}
