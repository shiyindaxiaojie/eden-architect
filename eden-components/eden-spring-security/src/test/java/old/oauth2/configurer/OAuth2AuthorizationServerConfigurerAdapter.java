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

package old.oauth2.configurer;

import old.oauth2.autoconfigure.OAuth2WebSecurityConfiguration;
import old.oauth2.constant.OAuth2Constants;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import old.oauth2.env.OAuth2Properties;

import java.util.List;

/**
 * OAuth2 授权服务器配置适配器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@AutoConfigureAfter(OAuth2WebSecurityConfiguration.class)
public class OAuth2AuthorizationServerConfigurerAdapter
	extends AuthorizationServerConfigurerAdapter {

	private final AuthenticationManager authenticationManager;

	private final TokenStore tokenStore;

	private final List<TokenEnhancer> tokenEnhancers;

	private final OAuth2Properties.Authorization oAuth2Properties;

	public OAuth2AuthorizationServerConfigurerAdapter(
		AuthenticationManager authenticationManager,
		TokenStore tokenStore,
		List<TokenEnhancer> tokenEnhancers,
		OAuth2Properties oAuth2Properties) {
		this.authenticationManager = authenticationManager;
		this.tokenStore = tokenStore;
		this.tokenEnhancers = tokenEnhancers;
		this.oAuth2Properties = oAuth2Properties.getAuthorization();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
		endpoints
			.authenticationManager(authenticationManager)
			.tokenStore(tokenStore)
			.tokenEnhancer(tokenEnhancerChain)
			.reuseRefreshTokens(false);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		oauthServer
			.tokenKeyAccess(OAuth2Constants.ACCESS_PERMIT_ALL)
			.checkTokenAccess(OAuth2Constants.ACCESS_IS_AUTHENTICATED)
			.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		OAuth2Properties.Authorization.Password password = oAuth2Properties.getPassword();
		OAuth2Properties.Authorization.ClientCredentials clientCredentials =
			oAuth2Properties.getClientCredentials();
		clients
			.inMemory()
			.withClient(password.getClientId())
			.secret(password.getClientSecret())
			.scopes(password.getScopes())
			.autoApprove(true)
			.authorizedGrantTypes(OAuth2Constants.GRANT_TYPE_PASSWORD, OAuth2Constants.REFRESH_TOKEN)
			.accessTokenValiditySeconds(password.getAccessTokenValiditySeconds())
			.refreshTokenValiditySeconds(password.getRefreshTokenValiditySeconds())
			.and()
			.withClient(clientCredentials.getClientId())
			.secret(clientCredentials.getClientSecret())
			.scopes(clientCredentials.getScopes())
			.autoApprove(true)
			.authorizedGrantTypes(OAuth2Constants.GRANT_TYPE_CLIENT_CREDENTIALS)
			.accessTokenValiditySeconds(clientCredentials.getAccessTokenValiditySeconds());
	}
}
