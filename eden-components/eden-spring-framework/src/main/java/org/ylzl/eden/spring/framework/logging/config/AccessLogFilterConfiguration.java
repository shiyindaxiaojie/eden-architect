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

package org.ylzl.eden.spring.framework.logging.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.ylzl.eden.spring.framework.logging.filter.AccessLogHttpFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 访问日志过滤器配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class AccessLogFilterConfiguration {

	@Bean
	public AccessLogHttpFilter accessLogHttpFilter(ObjectProvider<AccessLogConfig> accessLogConfigs) {
		AccessLogConfig config = accessLogConfigs.getIfUnique(AccessLogConfig::new);
		AccessLogHttpFilter httpFilter = new AccessLogHttpFilter();
		httpFilter.setSampleRate(config.getSampleRate());
		httpFilter.setMaxLength(config.getMaxLength());
		httpFilter.setEnabledMdc(config.isEnabledMdc());
		return httpFilter;
	}

	@WebFilter(filterName = "accessLogHttpFilter", urlPatterns = "/*")
	public static class CustomFilterRegistration implements Filter {

		@Autowired
		private AccessLogHttpFilter accessLogHttpFilter;

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
			accessLogHttpFilter.init(filterConfig);
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			accessLogHttpFilter.doFilter(request, response, chain);
		}

		@Override
		public void destroy() {
			accessLogHttpFilter.destroy();
		}
	}
}
