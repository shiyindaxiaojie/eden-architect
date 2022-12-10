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

package org.ylzl.eden.nacos.discovery.spring.cloud.autoconfigure;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.cloud.nacos.ribbon.NacosServerIntrospector;
import org.ylzl.eden.spring.cloud.nacos.ribbon.NacosServerList;

/**
 * NacosRibbonClient 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRibbonNacos
public class NacosRibbonClientConfiguration {

	private final PropertiesFactory propertiesFactory;

	public NacosRibbonClientConfiguration(PropertiesFactory propertiesFactory) {
		this.propertiesFactory = propertiesFactory;
	}

	@Bean
	@ConditionalOnMissingBean
	public ServerList<?> ribbonServerList(IClientConfig config,
										  NacosDiscoveryProperties nacosDiscoveryProperties) {
		if (this.propertiesFactory.isSet(ServerList.class, config.getClientName())) {
			ServerList serverList = this.propertiesFactory.get(ServerList.class, config,
				config.getClientName());
			return serverList;
		}
		NacosServerList serverList = new NacosServerList(nacosDiscoveryProperties);
		serverList.initWithNiwsConfig(config);
		return serverList;
	}

	@Bean
	@ConditionalOnMissingBean
	public NacosServerIntrospector nacosServerIntrospector() {
		return new NacosServerIntrospector();
	}
}
