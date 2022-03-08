package org.ylzl.eden.spring.cloud.nacos.ribbon;

import java.util.Map;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.loadbalancer.Server;

/**
 * NacosServer
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class NacosServer extends Server {

	private final MetaInfo metaInfo;

	private final Instance instance;

	private final Map<String, String> metadata;

	public NacosServer(final Instance instance) {
		super(instance.getIp(), instance.getPort());
		this.instance = instance;
		this.metaInfo = new MetaInfo() {
			@Override
			public String getAppName() {
				return instance.getServiceName();
			}

			@Override
			public String getServerGroup() {
				return null;
			}

			@Override
			public String getServiceIdForDiscovery() {
				return null;
			}

			@Override
			public String getInstanceId() {
				return instance.getInstanceId();
			}
		};
		this.metadata = instance.getMetadata();
	}

	@Override
	public MetaInfo getMetaInfo() {
		return metaInfo;
	}

	public Instance getInstance() {
		return instance;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}
}
