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

package org.ylzl.eden.dynamic.cache.integration.hotkey.sentinel;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.extension.ExtensionLoader;

/**
 * Sentinel热key探测客户端
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class SentinelHotKeyClient {

	/**
	 * 启动客户端
	 *
	 * @param cacheConfig 缓存配置
	 */
	public static void start(CacheConfig cacheConfig) {
		initSentinel(cacheConfig);
		registerDataSource(cacheConfig);
	}

	/**
	 * 初始化 Sentinel
	 *
	 * @param cacheConfig 缓存配置
	 */
	private static void initSentinel(CacheConfig cacheConfig) {
		CacheConfig.HotKey.Sentinel sentinelConfig = cacheConfig.getHotKey().getSentinel();
		if (StringUtils.isEmpty(System.getProperty(SentinelConfig.APP_NAME_PROP_KEY))) {
			System.setProperty(SentinelConfig.APP_NAME_PROP_KEY, sentinelConfig.getAppName());
		}
		if (StringUtils.isEmpty(System.getProperty(TransportConfig.SERVER_PORT)) &&
			StringUtils.isNotBlank(sentinelConfig.getTransport().getPort())) {
			System.setProperty(TransportConfig.SERVER_PORT, sentinelConfig.getTransport().getPort());
		}
		if (StringUtils.isEmpty(System.getProperty(TransportConfig.CONSOLE_SERVER)) &&
			StringUtils.isNotBlank(sentinelConfig.getTransport().getDashboard())) {
			System.setProperty(TransportConfig.CONSOLE_SERVER, sentinelConfig.getTransport().getDashboard());
		}
	}

	/**
	 * 注册规则到指定数据源
	 *
	 * @param cacheConfig 缓存配置
	 */
	private static void registerDataSource(CacheConfig cacheConfig) {
		SentinelHotKeyDataSource dataSource = ExtensionLoader.getExtensionLoader(SentinelHotKeyDataSource.class)
			.getExtension(cacheConfig.getHotKey().getSentinel().getDataSourceType());
		dataSource.registerDataSource(cacheConfig);
	}
}
