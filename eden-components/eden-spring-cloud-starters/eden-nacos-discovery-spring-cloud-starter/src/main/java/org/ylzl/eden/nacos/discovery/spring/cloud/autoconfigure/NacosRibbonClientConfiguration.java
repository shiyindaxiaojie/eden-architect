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
