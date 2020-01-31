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

package org.ylzl.eden.spring.boot.cloud.zuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.ReflectionUtils;
import org.ylzl.eden.spring.boot.cloud.zuul.ZuulConstants;
import org.ylzl.eden.spring.boot.cloud.zuul.ZuulProperties;
import org.ylzl.eden.spring.boot.security.core.util.SpringSecurityUtils;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.servlet.http.HttpServletRequest;

/**
 * 速率限制过滤器
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
public class RateLimitingFilter extends ZuulFilter {

    private static final String MSG_API_RATE_EXCEEDED = "API 速率超过限制";

    private static final String CACHE_NAME_SUFFIX = "-rate-limiting";

    private Cache<String, RateLimiter> cache;

    private final PathMatcher pathMatcher = new AntPathMatcher();

    private final ZuulProperties.RateLimiting properties;

    public RateLimitingFilter(ZuulProperties zuulProperties, String applicationName) {
        this.properties = zuulProperties.getRateLimiting();

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        CompleteConfiguration<String, RateLimiter> config =
            new MutableConfiguration<String, RateLimiter>().setTypes(String.class, RateLimiter.class);
        this.cache = cacheManager.createCache(applicationName + CACHE_NAME_SUFFIX, config);
    }

    @Override
    public String filterType() {
        return ZuulConstants.FILTER_TYPE_PRE;
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        String requestURI = RequestContext.getCurrentContext().getRequest().getRequestURI();
        String defaultIncludePattern = properties.getDefaultIncludePattern();
        return pathMatcher.match(defaultIncludePattern, requestURI);
    }

    @Override
    public Object run() {
        try {
            String key = getKey();
            RateLimiter rateLimiter = getRateLimiter(key);
            if (!rateLimiter.tryAcquire()) {
                apiLimitExceeded();
            }
        } catch (Exception e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        return null;
    }

    private String getKey() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        if (SpringSecurityUtils.getCurrentUserLogin() != null) {
            return SpringSecurityUtils.getCurrentUserLogin();
        }
        return request.getRemoteAddr();
    }

    private RateLimiter getRateLimiter(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        RateLimiter rateLimiter = RateLimiter.create(properties.getLimit());
        cache.put(key, rateLimiter);
        return rateLimiter;
    }

    private void apiLimitExceeded() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(MSG_API_RATE_EXCEEDED);
            ctx.setSendZuulResponse(false);
        }
    }
}
