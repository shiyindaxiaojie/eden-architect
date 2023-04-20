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

package org.ylzl.eden.spring.framework.logging.bootstrap.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProperties;
import org.ylzl.eden.spring.framework.logging.MdcConstants;
import org.ylzl.eden.spring.framework.web.util.ServletUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 引导日志过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class BootstrapLogHttpFilter extends HttpFilter {

	private boolean enabledMdc = false;

	private final Environment env;

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws IOException, ServletException {
		if (enabledMdc) {
			String appName = StringUtils.trimToEmpty(env.getProperty(SpringProperties.SPRING_APPLICATION_NAME));
			String profile = StringUtils.trimToEmpty(env.getProperty(SpringProperties.SPRING_PROFILE_DEFAULT));
			String requestURI = ServletUtils.getRequestURI(req);
			String remoteUser = ServletUtils.getRemoteUser(req);
			String remoteAddr = ServletUtils.getRemoteAddr(req);
			String localAddr = ServletUtils.getLocalAddr(req);

			MDC.put(MdcConstants.APP, appName);
			MDC.put(MdcConstants.PROFILE, profile);
			MDC.put(MdcConstants.REQUEST_URI, requestURI);
			MDC.put(MdcConstants.REMOTE_USER, remoteUser);
			MDC.put(MdcConstants.REMOTE_ADDR, remoteAddr);
			MDC.put(MdcConstants.LOCAL_ADDR, localAddr);
		}

		chain.doFilter(req, res);
	}

	public void setEnabledMdc(boolean enabledMdc) {
		this.enabledMdc = enabledMdc;
	}
}
