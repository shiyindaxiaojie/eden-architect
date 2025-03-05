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

package org.ylzl.eden.spring.cloud.zuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.util.PathMatcher;
import org.springframework.util.ReflectionUtils;
import org.ylzl.eden.spring.framework.json.support.JSONHelper;
import org.ylzl.eden.spring.framework.dto.extension.ResponseBuilder;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

/**
 * Zuul 速率限制
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Setter
@Getter
@RequiredArgsConstructor
@Slf4j
public class ZuulRateLimitingFilter extends ZuulFilter {

	private long permitsPerSecond = 100_000L;

	private Duration warmupPeriod = Duration.ofSeconds(1);

	private String cacheName = "app-rate-limiting";

	private final PathMatcher pathMatcher;

	private final String requestPattern;

	private final Cache<String, RateLimiter> cache;

	public ZuulRateLimitingFilter(PathMatcher pathMatcher, String requestPattern, CacheManager cacheManager) {
		this.pathMatcher = pathMatcher;
		this.requestPattern = requestPattern;

		CompleteConfiguration<String, RateLimiter> config =
			new MutableConfiguration<String, RateLimiter>().setTypes(String.class, RateLimiter.class);
		this.cache = cacheManager.createCache(cacheName, config);
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 10;
	}

	@Override
	public boolean shouldFilter() {
		String requestURI = RequestContext.getCurrentContext().getRequest().getRequestURI();
		return pathMatcher.match(requestPattern, requestURI);
	}

	@Override
	public Object run() {
		try {
			String key = getKey();
			RateLimiter rateLimiter = createOrGetRateLimiter(key);
			if (!rateLimiter.tryAcquire()) {
				requestLimitExceeded();
			}
		} catch (Exception e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		return null;
	}

	private String getKey() {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		return request.getRemoteAddr();
	}

	private RateLimiter createOrGetRateLimiter(String key) {
		if (cache.containsKey(key)) {
			return cache.get(key);
		}
		RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond, warmupPeriod);
		cache.put(key, rateLimiter);
		return rateLimiter;
	}

	private void requestLimitExceeded() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
		if (ctx.getResponseBody() == null) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseBody(JSONHelper.json().toJSONString(ResponseBuilder.builder().buildFailure("REQ-FLOW-429")));
		}
	}
}
