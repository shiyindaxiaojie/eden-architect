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
package org.ylzl.eden.zuul.spring.cloud.actuate;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关端点
 *
 * <p>从 Spring Boot 1.5.x 升级到 2.4.x</p>
 *
 * <ul>
 *   <li>org.springframework.boot.actuate.endpoint.AbstractEndpoint 变更为 {@link Endpoint}
 * </ul>
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Builder
@Slf4j
@Endpoint(id = ZuulRouteEndpoint.ENDPOINT_ID)
public class ZuulRouteEndpoint {

	public static final String ENDPOINT_ID = "zuul-routes";

	private final RouteLocator routeLocator;

	private final DiscoveryClient discoveryClient;

	public ZuulRouteEndpoint(RouteLocator routeLocator, DiscoveryClient discoveryClient) {
		this.routeLocator = routeLocator;
		this.discoveryClient = discoveryClient;
	}

	@ReadOperation
	public List<ZuulRoute> zuulRoutes() {
		List<Route> routes = routeLocator.getRoutes();
		List<ZuulRoute> zuulRoutes = new ArrayList<>();
		for (Route route : routes) {
			ZuulRoute zuulRoute = new ZuulRoute();
			zuulRoute.setPath(route.getFullPath());
			zuulRoute.setServiceId(route.getId());
			zuulRoute.setServiceInstances(discoveryClient.getInstances(route.getLocation()));
			zuulRoutes.add(zuulRoute);
		}
		return zuulRoutes;
	}
}
