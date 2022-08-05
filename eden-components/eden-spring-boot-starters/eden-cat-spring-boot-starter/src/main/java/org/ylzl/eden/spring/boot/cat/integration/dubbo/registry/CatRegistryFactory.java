package org.ylzl.eden.spring.boot.cat.integration.dubbo.registry;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;
import org.ylzl.eden.commons.lang.StringUtils;

import java.util.List;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class CatRegistryFactory implements RegistryFactory {

	public static final String PROVIDER_APPLICATION_NAME = "serverApplicationName";
	private final RegistryFactory registryFactory;

	@Override
	public Registry getRegistry(URL url) {
		return new RegistryWrapper(registryFactory.getRegistry(url));
	}

	public static String getProviderAppName(URL url) {
		String appName = url.getParameter(PROVIDER_APPLICATION_NAME);
		if (StringUtils.isBlank(appName)) {
			String interfaceName = url.getParameter(CommonConstants.INTERFACE_KEY);
			appName = interfaceName.substring(0, interfaceName.lastIndexOf('.'));
		}
		return appName;
	}

	static class RegistryWrapper implements Registry {
		private final Registry originRegistry;

		private URL appendProviderAppName(URL url) {
			String side = url.getParameter(CommonConstants.SIDE_KEY);
			if (CommonConstants.PROVIDER_SIDE.equals(side)) {
				url = url.addParameter(PROVIDER_APPLICATION_NAME, url.getParameter(CommonConstants.APPLICATION_KEY));
			}
			return url;
		}

		public RegistryWrapper(Registry originRegistry) {
			this.originRegistry = originRegistry;
		}

		@Override
		public URL getUrl() {
			return originRegistry.getUrl();
		}

		@Override
		public boolean isAvailable() {
			return originRegistry.isAvailable();
		}

		@Override
		public void destroy() {
			originRegistry.destroy();
		}

		@Override
		public void register(URL url) {
			originRegistry.register(appendProviderAppName(url));
		}

		@Override
		public void unregister(URL url) {
			originRegistry.unregister(appendProviderAppName(url));
		}

		@Override
		public void subscribe(URL url, NotifyListener listener) {
			originRegistry.subscribe(url, listener);
		}

		@Override
		public void unsubscribe(URL url, NotifyListener listener) {
			originRegistry.unsubscribe(url, listener);
		}

		@Override
		public List<URL> lookup(URL url) {
			return originRegistry.lookup(appendProviderAppName(url));
		}
	}
}
