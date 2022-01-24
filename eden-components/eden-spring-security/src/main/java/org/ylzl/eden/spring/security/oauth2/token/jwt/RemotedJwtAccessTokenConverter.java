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
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.ylzl.eden.spring.security.oauth2.env.OAuth2Properties;

import java.util.Map;

/**
 * JWT 访问令牌转换器
 *
 * @author gyl
 * @since 2.4.x
 */
@Slf4j
public class RemotedJwtAccessTokenConverter extends JwtAccessTokenConverter {

	private static final String MSG_EXPIRED_PUBLIC_KEY = "OAuth2 public key expired";

	private static final String MSG_REQ_PUBLIC_KEY =
		"Public key retrieved from OAuth2 server to create SignatureVerifier";

	private static final String MSG_REQ_PUBLIC_KEY_EXCEPTION =
		"Could not get public key from OAuth2 server to create SignatureVerifier, caught exception: {}";
	private final SignatureVerifierClient signatureVerifierClient;
	private final OAuth2Properties.Authorization oAuth2Properties;
	private long lastKeyFetchTimestamp = 0L;

	public RemotedJwtAccessTokenConverter(
		SignatureVerifierClient signatureVerifierClient, OAuth2Properties oAuth2Properties) {
		this.signatureVerifierClient = signatureVerifierClient;
		this.oAuth2Properties = oAuth2Properties.getAuthorization();
	}

	@Override
	protected Map<String, Object> decode(String token) {
		try {
			long ttl = oAuth2Properties.getPublicTokenKeyTtl();
			if (ttl > 0L
				&& lastKeyFetchTimestamp > 0L
				&& System.currentTimeMillis() - lastKeyFetchTimestamp > ttl) {
				throw new InvalidTokenException(MSG_EXPIRED_PUBLIC_KEY);
			}
			return super.decode(token);
		} catch (InvalidTokenException ex) {
			// 尝试获取公钥
			if (tryCreateSignatureVerifier()) {
				return super.decode(token);
			}
			throw ex;
		}
	}

	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
		OAuth2Authentication authentication = super.extractAuthentication(claims);
		authentication.setDetails(claims);
		return authentication;
	}

	private boolean tryCreateSignatureVerifier() {
		long t = System.currentTimeMillis();
		if (t - lastKeyFetchTimestamp < oAuth2Properties.getPublicTokenKeyRefreshRateLimit()) {
			return false;
		}
		try {
			SignatureVerifier verifier = signatureVerifierClient.createSignatureVerifier();
			if (verifier != null) {
				this.setVerifier(verifier);
				lastKeyFetchTimestamp = t;
				log.debug(MSG_REQ_PUBLIC_KEY);
				return true;
			}
		} catch (Exception ex) {
			log.error(MSG_REQ_PUBLIC_KEY_EXCEPTION, ex.getMessage(), ex);
		}
		return false;
	}
}
