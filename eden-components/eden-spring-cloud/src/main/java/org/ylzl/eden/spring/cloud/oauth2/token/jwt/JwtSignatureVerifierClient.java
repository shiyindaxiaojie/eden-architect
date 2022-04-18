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

package org.ylzl.eden.spring.cloud.oauth2.token.jwt;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.spring.cloud.loadbalancer.util.LoadBalancerClientHelper;
import org.ylzl.eden.spring.security.old.oauth2.env.OAuth2Properties;
import org.ylzl.eden.spring.security.old.oauth2.token.jwt.SignatureVerifierClientAdapter;

/**
 * 签名验证客户端
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class JwtSignatureVerifierClient extends SignatureVerifierClientAdapter {

	private final LoadBalancerClientHelper loadBalancerClientHelper;

	private final String publicKeyUriServiceId;

	public JwtSignatureVerifierClient(
		RestTemplate restTemplate,
		OAuth2Properties oAuth2Properties,
		LoadBalancerClientHelper loadBalancerClientHelper) {
		super(restTemplate, oAuth2Properties);
		this.loadBalancerClientHelper = loadBalancerClientHelper;
		this.publicKeyUriServiceId =
			oAuth2Properties.getAuthorization().getPublicTokenKeyUriServiceId();
	}

	@Override
	protected String getPublicKeyEndpointUri() {
		String publicKeyEndpointUri = super.getPublicKeyEndpointUri();
		if (StringUtils.isNotBlank(publicKeyUriServiceId)) {
			return loadBalancerClientHelper.reconstructURI(publicKeyUriServiceId, publicKeyEndpointUri);
		}
		return publicKeyEndpointUri;
	}
}
