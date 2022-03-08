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

package org.ylzl.eden.spring.integration.logstash.autoconfigure;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.framework.core.constant.SpringFrameworkConstants;
import org.ylzl.eden.spring.integration.core.constant.SpringIntegrationConstants;
import org.ylzl.eden.spring.integration.logstash.env.LogstashProperties;
import org.ylzl.eden.spring.integration.logstash.util.LogstashUtils;
import org.ylzl.eden.spring.integration.metrics.env.MetricsProperties;

import java.util.Map;

/**
 * Logstash 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnClass(LogstashEncoder.class)
@ConditionalOnExpression(LogstashAutoConfiguration.EXP_LOGSTASH_ENABLED)
@EnableConfigurationProperties(LogstashProperties.class)
@Slf4j
@Configuration
public class LogstashAutoConfiguration {

	public static final String EXP_LOGSTASH_ENABLED =
		"${" + SpringIntegrationConstants.PROP_PREFIX + ".logstash.enabled:true}";

	private static final String KEY_APP_NAME = "app_name";

	private static final String KEY_SERVER_PORT = "server_port";

	private static final String KEY_VERSION = "verison";

	private static final String KEY_TIMESTAMP = "timestamp";

	@Value(SpringFrameworkConstants.NAME_PATTERN)
	private String applicationName;

	public LogstashAutoConfiguration(
		@Value(SpringFrameworkConstants.NAME_PATTERN) String applicationName,
		@Value(SpringFrameworkConstants.PORT_PATTERN) String serverPort,
		LogstashProperties logstashProperties,
		ObjectProvider<MetricsProperties> metricsProperties,
		ObjectProvider<BuildProperties> buildProperties,
		ObjectMapper mapper)
		throws JsonProcessingException {

		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

		Map<String, String> map = Maps.newHashMap();
		map.put(KEY_APP_NAME, applicationName);
		map.put(KEY_SERVER_PORT, serverPort);
		buildProperties.ifAvailable(it -> map.put(KEY_VERSION, it.getVersion()));
		String customFields = mapper.writeValueAsString(map);

		if (logstashProperties.isUseJsonFormat()) {
			LogstashUtils.addJsonConsoleAppender(context, customFields);
		}
		if (logstashProperties.isEnabled()) {
			LogstashUtils.addLogstashTcpSocketAppender(context, customFields, logstashProperties);
		}
		if (logstashProperties.isUseJsonFormat() || logstashProperties.isEnabled()) {
			LogstashUtils.addContextListener(context, customFields, logstashProperties);
		}
		metricsProperties.ifAvailable(
			properties -> {
				if (properties.getLogs().isEnabled()) {
					LogstashUtils.setMetricsMarkerLogbackFilter(
						context, logstashProperties.isUseJsonFormat());
				}
			});
	}
}
