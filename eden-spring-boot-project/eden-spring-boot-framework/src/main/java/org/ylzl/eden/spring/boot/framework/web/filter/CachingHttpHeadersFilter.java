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

package org.ylzl.eden.spring.boot.framework.web.filter;

import org.apache.http.HttpHeaders;
import org.ylzl.eden.spring.boot.framework.core.FrameworkProperties;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 缓存 HTTP 头部信息过滤器
 *
 * @author gyl
 * @since 1.0.0
 */
public class CachingHttpHeadersFilter implements Filter {

  private static final long LAST_MODIFIED = System.currentTimeMillis();

  private long cacheTimeToLive = TimeUnit.DAYS.toMillis(365L);

  private final FrameworkProperties.Http properties;

  public CachingHttpHeadersFilter(FrameworkProperties frameworkProperties) {
    this.properties = frameworkProperties.getHttp();
  }

  @Override
  public void init(FilterConfig filterConfig) {
    cacheTimeToLive = TimeUnit.DAYS.toMillis(properties.getCache().getTimeToLiveInDays());
  }

  @Override
  public void destroy() {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpResponse.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=" + cacheTimeToLive + ", public");
    httpResponse.setHeader(HttpHeaders.PRAGMA, "cache");
    httpResponse.setDateHeader(HttpHeaders.EXPIRES, cacheTimeToLive + System.currentTimeMillis());
    httpResponse.setDateHeader(HttpHeaders.LAST_MODIFIED, LAST_MODIFIED);
  }
}
