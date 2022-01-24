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

package org.ylzl.eden.spring.cloud.loadbalancer.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.cloud.loadbalancer.util.LoadBalancerClientHelper;
import org.ylzl.eden.spring.framework.web.RestAutoConfiguration;

/**
 * 负载均衡客户端自动配置
 *
 * @author gyl
 * @since 2.4.x
 */
@AutoConfigureAfter(LoadBalancerAutoConfiguration.class)
@AutoConfigureBefore(RestAutoConfiguration.class)
@ConditionalOnBean(LoadBalancerClient.class)
@Slf4j
@Configuration
public class LoadBalancerClientAutoConfiguration {

	private static final String MSG_AUTOWIRED_LB_CLIENT_HELPER =
		"Autowired LoadBalancerClient helper";

	@ConditionalOnMissingBean
	@Bean
	public LoadBalancerClientHelper loadBalancerClientHelper(LoadBalancerClient loadBalancerClient) {
		log.debug(MSG_AUTOWIRED_LB_CLIENT_HELPER);
		return new LoadBalancerClientHelper(loadBalancerClient);
	}
}
