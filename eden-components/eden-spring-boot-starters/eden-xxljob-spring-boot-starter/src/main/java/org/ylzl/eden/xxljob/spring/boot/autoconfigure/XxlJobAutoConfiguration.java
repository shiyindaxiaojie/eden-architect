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

package org.ylzl.eden.xxljob.spring.boot.autoconfigure;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.util.IpUtil;
import com.xxl.job.core.util.NetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.commons.codec.digest.DigestUtils;
import org.ylzl.eden.xxljob.spring.boot.admin.AutoRegisterXxlJobExecutor;
import org.ylzl.eden.xxljob.spring.boot.admin.XxlJobAdminTemplate;
import org.ylzl.eden.xxljob.spring.boot.env.XxlJobProperties;

import java.io.File;
import java.math.BigInteger;

@ConditionalOnClass(XxlJobExecutor.class)
@ConditionalOnProperty(name = XxlJobProperties.ENABLED, havingValue = "true")
@EnableConfigurationProperties(XxlJobProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration
public class XxlJobAutoConfiguration {

	private static final String AUTOWIRED_XXL_JOB_EXECUTOR = "Autowired AutoRegisterXxlJobExecutor";

	public static final String AUTOWIRED_XXL_JOB_ADMIN_TEMPLATE = "Autowired XxlJobAdminTemplate";

	private static final int MAX_PORT = 65535;

	private final Environment environment;

	private final XxlJobProperties xxlJobProperties;

	@Bean(initMethod = "start", destroyMethod = "destroy")
	public XxlJobExecutor xxlJobExecutor(ObjectProvider<XxlJobAdminTemplate> xxlJobAdminTemplate) {
		log.info(AUTOWIRED_XXL_JOB_EXECUTOR);
		XxlJobExecutor xxlJobExecutor =
			xxlJobProperties.getAdmin().isAutoRegister() && xxlJobAdminTemplate.getIfAvailable() != null?
				new AutoRegisterXxlJobExecutor(xxlJobAdminTemplate.getObject()):
				new XxlJobExecutor();
		xxlJobExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());

		if (StringUtils.isNotBlank(xxlJobProperties.getAccessToken())) {
			xxlJobExecutor.setAccessToken(xxlJobProperties.getAccessToken());
		}

		String appName = resolveAppName(xxlJobProperties.getExecutor().getAppName());
		xxlJobExecutor.setAppName(appName);
		if (StringUtils.isNotBlank(xxlJobProperties.getExecutor().getAddress())) {
			xxlJobExecutor.setAdminAddresses(xxlJobProperties.getExecutor().getAddress());
		} else {
			xxlJobExecutor.setIp(resolveIp(xxlJobProperties.getExecutor().getIp()));
			xxlJobExecutor.setPort(resolvePort(xxlJobProperties.getExecutor().getPort()));
		}
		xxlJobExecutor.setLogPath(resolveLogPath(xxlJobProperties.getExecutor().getLogPath(), appName));
		xxlJobExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogRetentionDays());
		return xxlJobExecutor;
	}

	@ConditionalOnProperty(name = XxlJobProperties.PREFIX + ".admin.auto-register", havingValue = "true")
	@Bean
	public XxlJobAdminTemplate xxlJobAdminTemplate(RestTemplate restTemplate, XxlJobProperties xxlJobProperties) {
		log.debug(AUTOWIRED_XXL_JOB_ADMIN_TEMPLATE);
		String accessToken = generatorAdminToken();
		return new XxlJobAdminTemplate(restTemplate, accessToken, xxlJobProperties.getAdmin().getAddresses());
	}

	private String resolveAppName(String appName) {
		if (StringUtils.isNotBlank(appName)) {
			return appName;
		}

		return environment.getProperty("spring.application.name");
	}

	private String resolveIp(String ip) {
		if (StringUtils.isNotBlank(ip)) {
			return ip;
		}

		ip = IpUtil.getIp();
		log.warn("XxlJob auto get address: {}", ip);
		return ip;
	}

	private int resolvePort(Integer port) {
		if (port != null) {
			return port;
		}
		port = NetUtil.findAvailablePort(MAX_PORT);
		log.warn("XxlJob auto get port: {}", port);
		return port;
	}

	private String resolveLogPath(String logPath, String appName) {
		if (StringUtils.isNotBlank(logPath)) {
			return logPath;
		}
		String userHome = environment.getProperty("user.home");
		return StringUtils.join(userHome, File.separator, "logs", File.separator,
			"xxl-job", File.separator, appName);
	}

	private String generatorAdminToken() {
		String md5 = DigestUtils.md5Hex(xxlJobProperties.getAdmin().getLoginUsername()
			+ "_" + xxlJobProperties.getAdmin().getLoginPassword());
		return new BigInteger(1, md5.getBytes()).toString(16);
	}
}
