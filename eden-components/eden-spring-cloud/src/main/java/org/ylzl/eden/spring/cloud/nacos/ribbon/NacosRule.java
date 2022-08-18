package org.ylzl.eden.spring.cloud.nacos.ribbon;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基于 Ribbon 的 Nacos 策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class NacosRule extends AbstractLoadBalancerRule {

	private final NacosDiscoveryProperties nacosDiscoveryProperties;

	private final NacosServiceManager nacosServiceManager;

	public NacosRule(NacosDiscoveryProperties nacosDiscoveryProperties, NacosServiceManager nacosServiceManager) {
		this.nacosDiscoveryProperties = nacosDiscoveryProperties;
		this.nacosServiceManager = nacosServiceManager;
	}

	@Override
	public Server choose(Object key) {
		try {
			String clusterName = this.nacosDiscoveryProperties.getClusterName();
			String group = this.nacosDiscoveryProperties.getGroup();
			DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer) getLoadBalancer();
			String name = loadBalancer.getName();

			NamingService namingService = nacosServiceManager
				.getNamingService(nacosDiscoveryProperties.getNacosProperties());
			List<Instance> instances = namingService.selectInstances(name, group, true);
			if (CollectionUtils.isEmpty(instances)) {
				log.warn("no instance in service {}", name);
				return null;
			}

			List<Instance> instancesToChoose = instances;
			if (StringUtils.isNotBlank(clusterName)) {
				List<Instance> sameClusterInstances = instances.stream()
					.filter(instance -> Objects.equals(clusterName,
						instance.getClusterName()))
					.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(sameClusterInstances)) {
					instancesToChoose = sameClusterInstances;
				} else {
					log.warn(
						"A cross-cluster call occurs，name = {}, clusterName = {}, instance = {}",
						name, clusterName, instances);
				}
			}

			Instance instance = ExtendBalancer.getHostByRandomWeightExtend(instancesToChoose);

			return new NacosServer(instance);
		} catch (Exception e) {
			log.warn("NacosRule error", e);
			return null;
		}
	}

	/**
	 * Concrete implementation should implement this method so that the configuration set via
	 * {@link IClientConfig} (which in turn were set via Archaius properties) will be taken into consideration
	 *
	 * @param clientConfig
	 */
	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {
	}
}
