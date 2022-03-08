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

package org.ylzl.eden.spring.security.oauth2.token.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.spring.security.oauth2.env.OAuth2Properties;
import org.ylzl.eden.spring.security.oauth2.errors.InvalidPublicKeyException;

import java.util.Map;

/**
 * OAuth2 签名验证客户端适配器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class SignatureVerifierClientAdapter implements SignatureVerifierClient {

	public static final String SIGNATURE_VERIFIER_KEY = "value";
	private static final String MSG_NONE_PROP_PUBLIC_KEY_ENDPOINT =
		"No public key endpoint configured in oauth properties";
	private final RestTemplate restTemplate;

	private final OAuth2Properties.Authorization oAuth2Properties;

	public SignatureVerifierClientAdapter(
		RestTemplate restTemplate, OAuth2Properties oAuth2Properties) {
		this.restTemplate = restTemplate;
		this.oAuth2Properties = oAuth2Properties.getAuthorization();
	}

	@Override
	public SignatureVerifier createSignatureVerifier() {
		String publicKeyEndpointUri = getPublicKeyEndpointUri();
		if (StringUtils.isNotBlank(publicKeyEndpointUri)) {
			try {
				HttpEntity<Void> request = new HttpEntity<Void>(new HttpHeaders());
				String key =
					(String)
						restTemplate
							.exchange(publicKeyEndpointUri, HttpMethod.GET, request, Map.class)
							.getBody()
							.get(SIGNATURE_VERIFIER_KEY);
				return new RsaVerifier(key);
			} catch (Exception ex) {
				throw new InvalidPublicKeyException(ex.getMessage());
			}
		}
		return null;
	}

	protected String getPublicKeyEndpointUri() {
		String publicKeyEndpointUri = oAuth2Properties.getPublicTokenKeyUri();
		if (publicKeyEndpointUri == null) {
			throw new InvalidClientException(MSG_NONE_PROP_PUBLIC_KEY_ENDPOINT);
		}
		return publicKeyEndpointUri;
	}
}
