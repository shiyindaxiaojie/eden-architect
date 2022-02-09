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

package org.ylzl.eden.spring.security.oauth2.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.ylzl.eden.spring.security.core.autoconfigure.DefaultWebSecuirtyConfiguration;
import org.ylzl.eden.spring.security.core.constant.SpringSecurityConstants;
import org.ylzl.eden.spring.security.oauth2.configurer.OAuth2AuthorizationServerConfigurerAdapter;
import org.ylzl.eden.spring.security.oauth2.env.OAuth2Properties;
import org.ylzl.eden.spring.security.oauth2.token.jwt.JwtTokenEnhancer;

import java.security.KeyPair;
import java.util.List;

/**
 * OAuth2 授权服务端自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnClass(EnableAuthorizationServer.class)
@ConditionalOnExpression(OAuth2AuthorizationServerAutoConfiguration.EXP_OAUTH2_AUTHORIZATION_SERVER)
@EnableConfigurationProperties({OAuth2Properties.class})
@EnableAuthorizationServer
@Import({DefaultWebSecuirtyConfiguration.class, OAuth2WebSecurityConfiguration.class})
@Slf4j
@Configuration
public class OAuth2AuthorizationServerAutoConfiguration {

	public static final String EXP_OAUTH2_AUTHORIZATION_SERVER =
		"${" + SpringSecurityConstants.PROP_PREFIX + ".oauth2.authorization.server.enabled:false}";

	@ConditionalOnMissingBean
	@Bean
	public AuthorizationServerConfigurer authorizationServerConfigurer(
		AuthenticationManager authenticationManager,
		TokenStore tokenStore,
		List<TokenEnhancer> tokenEnhancers,
		OAuth2Properties oAuth2Properties) {
		return new OAuth2AuthorizationServerConfigurerAdapter(
			authenticationManager, tokenStore, tokenEnhancers, oAuth2Properties);
	}

	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}

	@ConditionalOnMissingBean(TokenStore.class)
	@Slf4j
	@Configuration
	public static class JwtOAuth2AuthorizationServerConfiguration {

		private static final String MSG_AUTOWIRED_JWT_ACCESS_TOKEN_CONVERTOR =
			"Autowired JwtAccessTokenConverter (OAuth2AuthorizationServer)";

		private static final String MSG_AUTOWIRED_TOKEN_STORE =
			"Autowired TokenStore (OAuth2AuthorizationServer JwtTokenStore)";

		private static final String MSG_AUTOWIRED_TOKEN_ENHANCER =
			"Autowired TokenEnhancer (OAuth2AuthorizationServer JwtTokenEnhancer)";

		private static final String BEAN_JWT_TOKEN_ENHANCER = "jwtTokenEnhancer";

		private final OAuth2Properties.KeyStore oAuth2Properties;

		public JwtOAuth2AuthorizationServerConfiguration(OAuth2Properties oAuth2Properties) {
			this.oAuth2Properties = oAuth2Properties.getKeyStore();
		}

		@ConditionalOnMissingBean
		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter() {
			log.debug(MSG_AUTOWIRED_JWT_ACCESS_TOKEN_CONVERTOR);
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			KeyPair keyPair =
				new KeyStoreKeyFactory(
					new ClassPathResource(oAuth2Properties.getName()),
					oAuth2Properties.getPassword().toCharArray())
					.getKeyPair(oAuth2Properties.getAlias());
			converter.setKeyPair(keyPair);
			return converter;
		}

		@Bean
		public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
			log.debug(MSG_AUTOWIRED_TOKEN_STORE);
			return new JwtTokenStore(jwtAccessTokenConverter);
		}

		@ConditionalOnMissingBean
		@Bean(BEAN_JWT_TOKEN_ENHANCER)
		public TokenEnhancer tokenEnhancer() {
			log.debug(MSG_AUTOWIRED_TOKEN_ENHANCER);
			return new JwtTokenEnhancer();
		}
	}

  /*@ConditionalOnProperty(prefix = SecurityConstants.PROP_PREFIX + ".oauth2", name = "authorization.token-store", havingValue = "redis")
  @Configuration
  public static class OAuth2AuthorizationServerRedisConfiguration {

      @ConditionalOnMissingBean
      @Bean
      public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
          return new RedisTokenStore(redisConnectionFactory);
      }
  }

  @ConditionalOnProperty(prefix = SecurityConstants.PROP_PREFIX + ".oauth2", name = "authorization.token-store", havingValue = "jdbc")
  @Configuration
  public static class OAuth2AuthorizationServerJdbcConfiguration {

      @ConditionalOnMissingBean
      @Bean
      public TokenStore tokenStore(DataSource dataSource) {
          return new JdbcTokenStore(dataSource);
      }
  }

  @ConditionalOnProperty(prefix = SecurityConstants.PROP_PREFIX + ".oauth2", name = "authorization.token-store", havingValue = "in-memory")
  @Configuration
  public static class OAuth2AuthorizationServerInMemoryConfiguration {

      @ConditionalOnMissingBean
      @Bean
      public TokenStore tokenStore() {
          return new InMemoryTokenStore();
      }
  }*/
}
