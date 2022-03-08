package org.ylzl.eden.spring.cloud.nacos.ribbon;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;

/**
 * NacosServerList
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class NacosServerList extends AbstractServerList<NacosServer> {

	private NacosDiscoveryProperties discoveryProperties;

	private String serviceId;

	public NacosServerList(NacosDiscoveryProperties discoveryProperties) {
		this.discoveryProperties = discoveryProperties;
	}

	@Override
	public List<NacosServer> getInitialListOfServers() {
		return getServers();
	}

	@Override
	public List<NacosServer> getUpdatedListOfServers() {
		return getServers();
	}

	private List<NacosServer> getServers() {
		try {
			String group = discoveryProperties.getGroup();
			List<Instance> instances = discoveryProperties.namingServiceInstance()
				.selectInstances(serviceId, group, true);
			return instancesToServerList(instances);
		}
		catch (Exception e) {
			throw new IllegalStateException(
				"Can not get service instances from nacos, serviceId=" + serviceId,
				e);
		}
	}

	private List<NacosServer> instancesToServerList(List<Instance> instances) {
		List<NacosServer> result = new ArrayList<>();
		if (CollectionUtils.isEmpty(instances)) {
			return result;
		}
		for (Instance instance : instances) {
			result.add(new NacosServer(instance));
		}

		return result;
	}

	public String getServiceId() {
		return serviceId;
	}

	@Override
	public void initWithNiwsConfig(IClientConfig iClientConfig) {
		this.serviceId = iClientConfig.getClientName();
	}
}
