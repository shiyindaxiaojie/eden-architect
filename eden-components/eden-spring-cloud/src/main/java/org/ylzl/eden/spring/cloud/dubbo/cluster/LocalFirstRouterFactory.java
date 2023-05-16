package org.ylzl.eden.spring.cloud.dubbo.cluster;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.cluster.Router;
import org.apache.dubbo.rpc.cluster.RouterFactory;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class LocalFirstRouterFactory implements RouterFactory {

	@Override
	public Router getRouter(URL url) {
		return null;
	}
}
