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

package org.ylzl.eden.full.tracing.integration.web;

import brave.Tracer;
import brave.baggage.BaggageField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.full.tracing.StressContext;
import org.ylzl.eden.full.tracing.constant.StressTag;

import javax.servlet.*;
import java.io.IOException;

/**
 * 压测标记过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class WebStressTagFilter implements Filter {

	private final Tracer tracer;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		StressContext stressContext = new StressContext();
		String tag = BaggageField.getByName(StressTag.STRESS_TAG).getValue();
		if (StringUtils.isNotBlank(tag)) {
			tracer.currentSpan().tag(StressTag.STRESS_TAG, tag);
			stressContext.setStress(Boolean.parseBoolean(tag));
		}
		StressContext.setContext(stressContext);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		StressContext.removeContext();
		Filter.super.destroy();
	}
}
