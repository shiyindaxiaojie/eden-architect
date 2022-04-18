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

package old.oauth2.token.jwt;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import old.jwt.constant.JwtConstants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JWT 令牌增强器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(
		OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		addClaims((DefaultOAuth2AccessToken) accessToken);
		return accessToken;
	}

	private void addClaims(DefaultOAuth2AccessToken accessToken) {
		DefaultOAuth2AccessToken token = accessToken;
		Map<String, Object> additionalInformation = token.getAdditionalInformation();
		if (additionalInformation.isEmpty()) {
			additionalInformation = new LinkedHashMap<>();
		}
		additionalInformation.put(
			JwtConstants.ISSUED_AT, new Integer((int) (System.currentTimeMillis() / 1000L)));
		token.setAdditionalInformation(additionalInformation);
	}
}
