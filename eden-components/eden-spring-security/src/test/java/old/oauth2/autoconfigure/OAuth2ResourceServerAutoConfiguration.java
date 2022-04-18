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

package old.oauth2.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.ylzl.eden.spring.framework.bootstrap.constant.GlobalConstants;
import old.oauth2.configurer.OAuth2ResourceServerConfigurerAdapter;
import old.oauth2.env.OAuth2Properties;

/**
 * OAuth2 资源服务器配置适配器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnClass(EnableResourceServer.class)
@ConditionalOnExpression(OAuth2ResourceServerAutoConfiguration.EXP_OAUTH2_RESOURCE_SERVER)
@EnableConfigurationProperties({OAuth2Properties.class})
@EnableResourceServer
@Slf4j
@Configuration
public class OAuth2ResourceServerAutoConfiguration {

	public static final String EXP_OAUTH2_RESOURCE_SERVER =
		"${" + GlobalConstants.PROP_EDEN_PREFIX + ".oauth2.resource.server.enabled:false}";

	@ConditionalOnMissingBean
	@Bean
	public ResourceServerConfigurer resourceServerConfigurer(TokenStore tokenStore) {
		return new OAuth2ResourceServerConfigurerAdapter(tokenStore);
	}
}
