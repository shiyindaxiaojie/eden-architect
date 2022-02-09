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

package org.ylzl.eden.spring.boot.xxljob.autoconfigure;

import com.xxl.job.core.executor.XxlJobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.boot.xxljob.env.XxlJobProperties;

/**
 * XXLJob 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnExpression("${xxl.job.enabled:false}")
@EnableConfigurationProperties(XxlJobProperties.class)
@Slf4j
@Configuration
public class XxlJobAutoCofiguration {

	private final XxlJobProperties xxlJobProperties;

	public XxlJobAutoCofiguration(XxlJobProperties xxlJobProperties) {
		this.xxlJobProperties = xxlJobProperties;
	}

	@Bean(initMethod = "start", destroyMethod = "destroy")
	public XxlJobExecutor xxlJobExecutor() {
		log.info("Autowired XxlJobExecutor");
		XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
		xxlJobExecutor.setAccessToken(xxlJobProperties.getAccessToken());
		xxlJobExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
		xxlJobExecutor.setAppname(xxlJobProperties.getExecutor().getAppName());
		xxlJobExecutor.setIp(xxlJobProperties.getExecutor().getIp());
		xxlJobExecutor.setPort(xxlJobProperties.getExecutor().getPort());
		xxlJobExecutor.setLogPath(xxlJobProperties.getExecutor().getLogPath());
		xxlJobExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogRetentionDays());
		return xxlJobExecutor;
	}
}
