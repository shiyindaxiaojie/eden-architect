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

package org.ylzl.eden.ftpclient.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.ftpclient.spring.boot.env.FTPClientProperties;
import org.ylzl.eden.spring.integration.ftpclient.config.FTPClientConfig;
import org.ylzl.eden.spring.integration.ftpclient.pool2.FTPClientPool;
import org.ylzl.eden.spring.integration.ftpclient.core.FTPClientTemplate;
import org.ylzl.eden.spring.integration.ftpclient.pool2.FTPClientPool2;
import org.ylzl.eden.spring.integration.ftpclient.pool2.config.FTPClientPool2Config;
import org.ylzl.eden.spring.integration.ftpclient.pool2.factory.FTPClientPool2Factory;

/**
 * FTPClient 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(value = FTPClientProperties.PREFIX + ".enabled", matchIfMissing = true)
@ConditionalOnClass(FTPClient.class)
@EnableConfigurationProperties(FTPClientProperties.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class FTPClientAutoConfiguration {

	private static final String MSG_AUTOWIRED_FTP_CLIENT = "Autowired FTPClientTemplate";

	@ConditionalOnMissingBean
	@Bean
	public FTPClientTemplate ftpClientTemplate(FTPClientPool pool) {
		log.debug(MSG_AUTOWIRED_FTP_CLIENT);
		return new FTPClientTemplate(pool);
	}

	@ConditionalOnClass(GenericObjectPool.class)
	@Configuration(proxyBeanMethods = false)
	public static class FTPClientPool2AutoConfiguration {

		private static final String MSG_AUTOWIRED_FTP_CLIENT_POOL2 = "Autowired FTPClient pool2";

		private static final String MSG_AUTOWIRED_FTP_CLIENT_POOL2_FACTORY =
			"Autowired FTPClient pool2 factory";

		private final FTPClientProperties properties;

		public FTPClientPool2AutoConfiguration(FTPClientProperties properties) {
			this.properties = properties;
		}

		@ConditionalOnMissingBean
		@Bean
		public FTPClientPool2Factory ftpClientFactory() {
			log.debug(MSG_AUTOWIRED_FTP_CLIENT_POOL2_FACTORY);
			FTPClientConfig config = new FTPClientConfig();
			config.setHost(properties.getHost());
			config.setPort(properties.getPort());
			config.setUsername(properties.getUsername());
			config.setPassword(properties.getPassword());
			config.setConnectTimeOut(properties.getConnectTimeOut());
			config.setDataTimeout(properties.getDataTimeout());
			config.setControlEncoding(properties.getControlEncoding());
			config.setBufferSize(properties.getBufferSize());
			config.setFileType(properties.getFileType());
			config.setUseEPSVwithIPv4(properties.isUseEPSVwithIPv4());
			config.setPassiveMode(properties.isPassiveMode());
			return new FTPClientPool2Factory(config);
		}

		@ConditionalOnMissingBean
		@Bean
		public FTPClientPool ftpClientPool2(FTPClientPool2Factory factory) {
			log.debug(MSG_AUTOWIRED_FTP_CLIENT_POOL2);
			FTPClientPool2Config config = new FTPClientPool2Config();
			config.setMinIdle(properties.getPool().getMinIdle());
			config.setMaxIdle(properties.getPool().getMaxIdle());
			config.setMaxTotal(properties.getPool().getMaxTotal());
			config.setMaxWaitMillis(properties.getPool().getMaxWaitMillis());
			return new FTPClientPool2(factory, config);
		}
	}
}
