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

package org.ylzl.eden.spring.framework.logging.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.framework.logging.util.AccessLogHelper;
import org.ylzl.eden.spring.framework.web.util.CustomHttpServletResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * 访问日志过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class AccessLogHttpFilter extends HttpFilter {

	public static final String ENABLED_MDC = "enabledMdc";

	public static final String SAMPLE_RATE = "sampleRate";

	public static final String MAX_LENGTH = "maxLength";

	public static final String SLOW_LOG = "slowLog";

	private boolean enabledMdc = false;

	private double sampleRate = 1;

	private int maxLength = 500;

	private long slowLog = 1000;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String enabledMdc = filterConfig.getInitParameter(ENABLED_MDC);
		if (StringUtils.isNotBlank(enabledMdc)) {
			this.enabledMdc = Boolean.parseBoolean(enabledMdc);
		}

		String sampleRate = filterConfig.getInitParameter(SAMPLE_RATE);
		if (StringUtils.isNotBlank(sampleRate)) {
			this.sampleRate = Double.parseDouble(sampleRate);
		}

		String maxLength = filterConfig.getInitParameter(MAX_LENGTH);
		if (StringUtils.isNotBlank(enabledMdc)) {
			this.maxLength = Integer.parseInt(maxLength);
		}

		String slowLog = filterConfig.getInitParameter(SLOW_LOG);
		if (StringUtils.isNotBlank(slowLog)) {
			this.slowLog = Long.parseLong(slowLog);
		}
	}

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException {
		if (!AccessLogHelper.shouldLog(sampleRate)) {
			chain.doFilter(req, res);
			return;
		}

		CustomHttpServletResponseWrapper responseWrapper = new CustomHttpServletResponseWrapper(res);
		Instant start = Instant.now();
		Throwable throwable = null;
		try {
			chain.doFilter(req, responseWrapper);
		} catch (Throwable t) {
			throwable = t;
			throw t;
		} finally {
			long duration = Duration.between(start, Instant.now()).toMillis();
			AccessLogHelper.log(req, responseWrapper, throwable, duration, enabledMdc, maxLength, slowLog);
		}
	}

	public void setEnabledMdc(boolean enabledMdc) {
		this.enabledMdc = enabledMdc;
	}

	public void setSampleRate(double sampleRate) {
		this.sampleRate = sampleRate;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}
