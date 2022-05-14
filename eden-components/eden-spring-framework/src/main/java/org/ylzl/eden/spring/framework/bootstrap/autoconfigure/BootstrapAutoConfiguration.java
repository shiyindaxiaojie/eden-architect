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

package org.ylzl.eden.spring.framework.bootstrap.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;
import org.ylzl.eden.spring.framework.bootstrap.bind.BinderHelper;

import javax.servlet.http.HttpServlet;

/**
 * Spring 内置工具自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnClass(HttpServlet.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Configuration
public class BootstrapAutoConfiguration {

	@ConditionalOnMissingBean
	@Bean
	public PathMatcher pathMatcher() {
		return new AntPathMatcher();
	}

	@ConditionalOnMissingBean
	@Bean
	public UrlPathHelper urlPathHelper() {
		return new UrlPathHelper();
	}

	@ConditionalOnMissingBean
	@Bean
	public PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver() {
		return new PathMatchingResourcePatternResolver();
	}

	@ConditionalOnMissingBean
	@Bean
	public BinderHelper binderHelper(Environment environment) {
		return new BinderHelper(environment);
	}
}