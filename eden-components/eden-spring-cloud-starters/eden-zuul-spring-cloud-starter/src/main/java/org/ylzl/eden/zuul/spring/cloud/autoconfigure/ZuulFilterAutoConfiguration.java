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

package org.ylzl.eden.zuul.spring.cloud.autoconfigure;

import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.PathMatcher;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;
import org.ylzl.eden.zuul.spring.cloud.env.ZuulProperties;
import org.ylzl.eden.spring.cloud.zuul.filter.ZuulAccessControlFilter;
import org.ylzl.eden.spring.cloud.zuul.filter.ZuulFaultFilter;
import org.ylzl.eden.spring.cloud.zuul.filter.ZuulRateLimitingFilter;

import javax.cache.CacheManager;

/**
 * Zuul 过滤器自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = ZuulProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE
)
@ConditionalOnClass(ZuulFilter.class)
@EnableConfigurationProperties(ZuulProperties.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class ZuulFilterAutoConfiguration {

	private static final String AUTOWIRED_ACCESS_CONTROL_FILTER = "Autowired Zuul AccessControl Filter";

	private static final String AUTOWIRED_RATE_LIMIT_FILTER = "Autowired Zuul RateLimit Filter";

	private static final String AUTOWIRED_ZUUL_FAULT_FILTER = "Autowired Zuul Fault Filter";

	private final ZuulProperties zuulProperties;

	public ZuulFilterAutoConfiguration(ZuulProperties zuulProperties) {
		this.zuulProperties = zuulProperties;
	}

	@ConditionalOnProperty(
		prefix = ZuulProperties.AccessControl.PREFIX,
		name = Conditions.ENABLED,
		havingValue = Conditions.TRUE
	)
	@ConditionalOnBean(RouteLocator.class)
	@ConditionalOnMissingBean
	@Bean
	public ZuulAccessControlFilter accessControlFilter(RouteLocator routeLocator) {
		log.debug(AUTOWIRED_ACCESS_CONTROL_FILTER);
		return new ZuulAccessControlFilter(routeLocator, zuulProperties.getAccessControl().getAuthorizedEndpoints());
	}

	@ConditionalOnProperty(
		prefix = ZuulProperties.RateLimiting.PREFIX,
		name = Conditions.ENABLED,
		havingValue = Conditions.TRUE
	)
	@ConditionalOnMissingBean
	@Bean
	public ZuulRateLimitingFilter rateLimitingFilter(PathMatcher pathMatcher,
													 CacheManager cacheManager) {
		log.debug(AUTOWIRED_RATE_LIMIT_FILTER);
		return new ZuulRateLimitingFilter(pathMatcher, zuulProperties.getRateLimiting().getRequestPattern(), cacheManager);
	}

	@ConditionalOnMissingBean
	@Bean
	public ZuulFaultFilter zuulFaultFilter() {
		log.debug(AUTOWIRED_ZUUL_FAULT_FILTER);
		return new ZuulFaultFilter();
	}
}
