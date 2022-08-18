package org.ylzl.eden.spring.cloud.nacos.ribbon;

import com.netflix.loadbalancer.Server;
import org.springframework.cloud.netflix.ribbon.DefaultServerIntrospector;

import java.util.Map;

/**
 * NacosServerIntrospector
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class NacosServerIntrospector extends DefaultServerIntrospector {

	@Override
	public Map<String, String> getMetadata(Server server) {
		if (server instanceof NacosServer) {
			return ((NacosServer) server).getMetadata();
		}
		return super.getMetadata(server);
	}

	@Override
	public boolean isSecure(Server server) {
		if (server instanceof NacosServer) {
			return Boolean.parseBoolean(((NacosServer) server).getMetadata().get("secure"));
		}

		return super.isSecure(server);
	}
}
