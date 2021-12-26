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

package org.ylzl.eden.spring.cloud.oauth2.token;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.ylzl.eden.spring.cloud.loadbalancer.util.LoadBalancerClientHelper;
import org.ylzl.eden.spring.security.oauth2.env.OAuth2Properties;
import org.ylzl.eden.spring.security.oauth2.token.OAuth2AccessTokenClientAdapter;

/**
 * OAuth2 访问令牌客户端
 *
 * @author gyl
 * @since 1.0.0
 */
public class CloudOAuth2AccessTokenClient extends OAuth2AccessTokenClientAdapter {

  private final LoadBalancerClientHelper loadBalancerClientHelper;

  private final String accessTokenUriServiceId;

  public CloudOAuth2AccessTokenClient(
      RestTemplate restTemplate,
      OAuth2Properties oAuth2Properties,
      LoadBalancerClientHelper loadBalancerClientHelper) {
    super(restTemplate, oAuth2Properties);
    this.loadBalancerClientHelper = loadBalancerClientHelper;
    this.accessTokenUriServiceId = oAuth2Properties.getAuthorization().getAccessTokenUriServiceId();
  }

  /**
   * 获取 OAuth2 访问令牌端点
   *
   * @return OAuth2 访问令牌端点
   */
  @Override
  protected String getAccessTokenUri() {
    String accessTokenUri = super.getAccessTokenUri();
    if (StringUtils.isNotBlank(accessTokenUriServiceId)) {
      return loadBalancerClientHelper.reconstructURI(accessTokenUriServiceId, accessTokenUri);
    }
    return accessTokenUri;
  }
}
