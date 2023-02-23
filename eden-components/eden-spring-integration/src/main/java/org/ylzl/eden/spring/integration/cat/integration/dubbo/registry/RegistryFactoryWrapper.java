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

package org.ylzl.eden.spring.integration.cat.integration.dubbo.registry;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;
import org.ylzl.eden.commons.lang.StringUtils;

import java.util.List;

/**
 * CAT 包装 RegistryFactory
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class RegistryFactoryWrapper implements RegistryFactory {

	private static final String PROVIDER_APPLICATION_KEY = "provider.application";

	private final RegistryFactory registryFactory;

	@Override
	public Registry getRegistry(URL url) {
		return new RegistryWrapper(registryFactory.getRegistry(url));
	}

	public static String getProviderAppName(URL url) {
		// 消费方从提供方传递的参数获取应用名
		String appName = url.getParameter(PROVIDER_APPLICATION_KEY);
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
				// 让提供方传递应用名参数 -> CatRegistryFactoryWrapper.getProviderAppName
				url = url.addParameter(PROVIDER_APPLICATION_KEY, url.getParameter(CommonConstants.APPLICATION_KEY));
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
