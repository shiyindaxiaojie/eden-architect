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
package org.ylzl.eden.spring.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * Zuul 异常捕获处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Component
public class ZuulFaultFilter extends ZuulFilter {

	private static final String ZUUL_FILTER_FAULT = "Zuul filter caught exception: {}";

	private static final String ZUUL_FILTER_EXCEPTION = "Zuul filter response failed, caught exception: {}";

	@Override
	public String filterType() {
		return FilterConstants.ERROR_TYPE;
	}

	@Override
	public int filterOrder() {
		return -1;
	}

	@Override
	public boolean shouldFilter() {
		return RequestContext.getCurrentContext().getThrowable() != null;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		try {
			Throwable throwable = ctx.getThrowable();
			if (throwable != null) {
				log.error(ZUUL_FILTER_FAULT, throwable.getMessage(), throwable);
				ctx.setResponseBody(MessageFormatUtils.format(ZUUL_FILTER_FAULT, throwable.getMessage()));
				ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
				ctx.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
		} catch (Exception ex) {
			log.error(ZUUL_FILTER_EXCEPTION, ex.getMessage(), ex);
			ReflectionUtils.rethrowRuntimeException(ex);
		}
		return null;
	}
}
