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

package org.ylzl.eden.spring.cloud.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.spring.cloud.loadbalancer.autoconfigure.LoadBalancerClientHelperAutoConfiguration;
import org.ylzl.eden.spring.cloud.loadbalancer.util.LoadBalancerClientHelper;
import org.ylzl.eden.spring.cloud.oauth2.token.JwtClientCredentialsResourceDetails;
import org.ylzl.eden.spring.cloud.oauth2.token.JwtOAuth2AccessTokenClient;
import org.ylzl.eden.spring.cloud.oauth2.token.jwt.JwtSignatureVerifierClient;
import org.ylzl.eden.spring.framework.web.autoconfigure.CustomRestTemplateAutoConfiguration;
import org.ylzl.eden.spring.security.oauth2.autoconfigure.OAuth2AuthorizationClientAutoConfiguration;
import org.ylzl.eden.spring.security.oauth2.env.OAuth2Properties;
import org.ylzl.eden.spring.security.oauth2.token.OAuth2AccessTokenClient;
import org.ylzl.eden.spring.security.oauth2.token.jwt.SignatureVerifierClient;

/**
 * 负载均衡的 OAuth2 授权客户端自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@AutoConfigureAfter({CustomRestTemplateAutoConfiguration.class, LoadBalancerClientHelperAutoConfiguration.class})
@AutoConfigureBefore(OAuth2AuthorizationClientAutoConfiguration.class)
@ConditionalOnExpression(OAuth2AuthorizationClientAutoConfiguration.EXP_OAUTH2_AUTHORIZATION_CLIENT)
@EnableConfigurationProperties({OAuth2Properties.class})
@Import(OAuth2AuthorizationClientAutoConfiguration.class)
@Slf4j
@Configuration
public class LoadBalancedOAuth2AuthorizationClientAutoConfiguration {

	@Configuration
	public static class LoadBalancedOAuth2AuthorizationClientInnerConfiguration {

		private static final String MSG_AUTOWIRED_OAUTH2_ACCESS_TOKEN_CLIENT =
			"Autowired loadBalanced OAuth2AccessTokenClient";

		private static final String MSG_AUTOWIRED_CLIENT_RESOURCE_DETAILS =
			"Autowired loadBalanced ClientCredentialsResourceDetails";

		private final OAuth2Properties oAuth2Properties;

		private final LoadBalancerClientHelper loadBalancerClientHelper;

		public LoadBalancedOAuth2AuthorizationClientInnerConfiguration(
			OAuth2Properties oAuth2Properties, LoadBalancerClientHelper loadBalancerClientHelper) {
			this.oAuth2Properties = oAuth2Properties;
			this.loadBalancerClientHelper = loadBalancerClientHelper;
		}

		@ConditionalOnMissingBean
		@Bean
		public OAuth2AccessTokenClient oAuth2AccessTokenClient(RestTemplate restTemplate) {
			log.debug(MSG_AUTOWIRED_OAUTH2_ACCESS_TOKEN_CLIENT);
			return new JwtOAuth2AccessTokenClient(
				restTemplate, oAuth2Properties, loadBalancerClientHelper);
		}

		@ConditionalOnClass({ClientCredentialsResourceDetails.class, LoadBalancerClient.class})
		@ConditionalOnMissingBean
		@Bean
		public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
			log.debug(MSG_AUTOWIRED_CLIENT_RESOURCE_DETAILS);
			JwtClientCredentialsResourceDetails resourceDetails =
				new JwtClientCredentialsResourceDetails(oAuth2Properties, loadBalancerClientHelper);
			resourceDetails.setAccessTokenUri(oAuth2Properties.getAuthorization().getAccessTokenUri());
			resourceDetails.setClientId(
				oAuth2Properties.getAuthorization().getClientCredentials().getClientId());
			resourceDetails.setClientSecret(
				oAuth2Properties.getAuthorization().getClientCredentials().getClientSecret());
			return resourceDetails;
		}
	}

	@Configuration
	public static class LoadBalancedOAuth2AuthorizationClientJwtConfiguration {

		private static final String MSG_AUTOWIRED_SIGN_VERIFY_CLIENT =
			"Autowired loadBalanced SignatureVerifierClient";

		private final OAuth2Properties oAuth2Properties;

		private final LoadBalancerClientHelper loadBalancerClientHelper;

		public LoadBalancedOAuth2AuthorizationClientJwtConfiguration(
			OAuth2Properties oAuth2Properties, LoadBalancerClientHelper loadBalancerClientHelper) {
			this.oAuth2Properties = oAuth2Properties;
			this.loadBalancerClientHelper = loadBalancerClientHelper;
		}

		@ConditionalOnMissingBean
		@Bean
		public SignatureVerifierClient signatureVerifierClient(RestTemplate restTemplate) {
			log.debug(MSG_AUTOWIRED_SIGN_VERIFY_CLIENT);
			return new JwtSignatureVerifierClient(
				restTemplate, oAuth2Properties, loadBalancerClientHelper);
		}
	}
}
