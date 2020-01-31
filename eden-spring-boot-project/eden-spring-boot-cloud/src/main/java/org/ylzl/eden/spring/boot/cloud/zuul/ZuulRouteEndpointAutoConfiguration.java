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

package org.ylzl.eden.spring.boot.cloud.zuul;

import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.cloud.zuul.endpoint.ZuulRouteEndpoint;

/**
 * 网关路由端点自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@ConditionalOnBean({RouteLocator.class, DiscoveryClient.class})
@ConditionalOnClass({ZuulFilter.class})
@ConditionalOnEnabledEndpoint(ZuulRouteEndpoint.ENDPOINT_ID)
@Slf4j
@Configuration
public class ZuulRouteEndpointAutoConfiguration {

	private static final String MSG_INJECT_ZUUL_ROUTE_ENDPOINT = "Inject Zuul custom actuator";

    @ConditionalOnMissingBean
    @Bean
    public ZuulRouteEndpoint zuulRouteEndpoint(RouteLocator routeLocator, DiscoveryClient discoveryClient) {
    	log.debug(MSG_INJECT_ZUUL_ROUTE_ENDPOINT);
    	return new ZuulRouteEndpoint(routeLocator, discoveryClient);
	}
}
