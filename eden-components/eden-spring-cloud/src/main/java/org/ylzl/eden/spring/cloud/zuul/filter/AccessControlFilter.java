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
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.http.HttpStatus;
import org.ylzl.eden.spring.cloud.zuul.constant.ZuulConstants;
import org.ylzl.eden.spring.cloud.zuul.env.ZuulProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 访问控制过滤器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class AccessControlFilter extends ZuulFilter {

	private static final String MSG_ALLOWING_ACCESS =
		"Access Control: allowing access for {}, as no access control policy has been set up for service: {}";

	private static final String MSG_FILTER_UNAUTHORIZED_ACCESS =
		"Access Control: filtered unauthorized access on endpoint {}";

	private final ZuulProperties zuulProperties;

	private final RouteLocator routeLocator;

	public AccessControlFilter(ZuulProperties zuulProperties, RouteLocator routeLocator) {
		this.zuulProperties = zuulProperties;
		this.routeLocator = routeLocator;
	}

	@Override
	public String filterType() {
		return ZuulConstants.FILTER_TYPE_PRE;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		for (Route route : routeLocator.getRoutes()) {
			String serviceUrl = request.getContextPath() + route.getFullPath();
			String serviceName = route.getId();
			if (request.getRequestURI().startsWith(serviceUrl.substring(0, serviceUrl.length() - 2))) {
				return !isAuthorizedRequest(serviceUrl, serviceName, request.getRequestURI());
			}
		}
		return false;
	}

	private boolean isAuthorizedRequest(String serviceUrl, String serviceName, String requestUri) {
		Map<String, List<String>> authorizedMicroservicesEndpoints =
			zuulProperties.getAccessControl().getAuthorizedMicroservicesEndpoints();

		List<String> authorizedEndpoints = authorizedMicroservicesEndpoints.get(serviceName);
		if (authorizedEndpoints == null) {
			log.debug(MSG_ALLOWING_ACCESS, requestUri, serviceName);
			return true;
		}
		for (String endpoint : authorizedEndpoints) {
			String gatewayEndpoint = serviceUrl.substring(0, serviceUrl.length() - 3) + endpoint;
			if (requestUri.startsWith(gatewayEndpoint)) {
				log.debug(MSG_ALLOWING_ACCESS, requestUri, gatewayEndpoint);
				return true;
			}
		}
		return false;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
		if (ctx.getResponseBody() == null && !ctx.getResponseGZipped()) {
			ctx.setSendZuulResponse(false);
		}
		log.debug(MSG_FILTER_UNAUTHORIZED_ACCESS, ctx.getRequest().getRequestURI());
		return null;
	}
}
