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

package org.ylzl.eden.spring.cloud.nacos.ribbon;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于 Nacos 随机&权重的策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class NacosRandomWithWeightRule extends AbstractLoadBalancerRule {

	private final NacosDiscoveryProperties nacosDiscoveryProperties;

	public NacosRandomWithWeightRule(NacosDiscoveryProperties nacosDiscoveryProperties) {
		this.nacosDiscoveryProperties = nacosDiscoveryProperties;
	}

	@Override
	public Server choose(Object key) {
		DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer) getLoadBalancer();
		String serviceName = loadBalancer.getName();
		NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
		try {
			Instance instance = namingService.selectOneHealthyInstance(serviceName);
			return new NacosServer(instance);
		} catch (NacosException e) {
			log.error("获取服务实例异常：{}", e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {
	}
}
