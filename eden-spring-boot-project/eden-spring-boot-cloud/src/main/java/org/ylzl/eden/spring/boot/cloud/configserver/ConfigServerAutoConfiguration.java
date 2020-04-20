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

package org.ylzl.eden.spring.boot.cloud.configserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.environment.NativeEnvironmentProperties;
import org.springframework.cloud.config.server.environment.NativeEnvironmentRepository;
import org.springframework.cloud.config.server.environment.NativeEnvironmentRepositoryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;
import org.ylzl.eden.spring.boot.framework.core.ProfileConstants;

/**
 * ConfigServer 自动配置
 *
 * @author gyl
 * @since 1.0.0
 */
@ConditionalOnClass({EnableConfigServer.class})
@ConditionalOnExpression(ConfigServerAutoConfiguration.EXPS_CONFIG_SERVER_ENABLED)
@EnableConfigServer
@Profile(ProfileConstants.SPRING_PROFILE_NATIVE)
@Slf4j
@Configuration
public class ConfigServerAutoConfiguration {

	public static final String EXPS_CONFIG_SERVER_ENABLED = "${" + FrameworkConstants.PROP_SPRING_PREFIX + ".cloud.config.server.bootstrap:true}";

	private final NativeEnvironmentRepositoryFactory nativeEnvironmentRepositoryFactory;

	public ConfigServerAutoConfiguration(NativeEnvironmentRepositoryFactory nativeEnvironmentRepositoryFactory) {
		this.nativeEnvironmentRepositoryFactory = nativeEnvironmentRepositoryFactory;
	}

	@ConditionalOnMissingBean
	@Bean
	public NativeEnvironmentRepository nativeEnvironmentRepository(NativeEnvironmentRepositoryFactory factory, NativeEnvironmentProperties environmentProperties) {
		return factory.build(environmentProperties);
	}
}
