/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.ftpserver.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.config.spring.factorybeans.FtpServerFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.ftpserver.spring.boot.env.FTPServerProperties;

/**
 * FtpServer 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(value = FTPServerProperties.PREFIX + ".enabled", matchIfMissing = true)
@ConditionalOnClass(FtpServer.class)
@EnableConfigurationProperties(FTPServerProperties.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class FTPServerAutoConfiguration {

	private static final String MSG_AUTOWIRED_FTP_SERVER = "Autowired FTPServer";

	private static final String MSG_AUTOWIRED_FTP_SERVER_FACTORY = "Autowired FTPServer Factory";

	@ConditionalOnMissingBean
	@Bean
	public FtpServerFactoryBean ftpServerFactoryBean() {
		log.debug(MSG_AUTOWIRED_FTP_SERVER_FACTORY);
		return new FtpServerFactoryBean();
	}

	@ConditionalOnMissingBean
	@Bean
	public FtpServer ftpServer(FtpServerFactoryBean ftpServerFactoryBean) {
		log.debug(MSG_AUTOWIRED_FTP_SERVER);
		// TODO
		return ftpServerFactoryBean.createServer();
	}
}
