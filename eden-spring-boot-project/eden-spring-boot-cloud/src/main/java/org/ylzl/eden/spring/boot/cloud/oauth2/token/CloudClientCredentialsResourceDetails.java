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

package org.ylzl.eden.spring.boot.cloud.oauth2.token;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.ylzl.eden.spring.boot.cloud.loadbalancer.util.LoadBalancerClientHelper;
import org.ylzl.eden.spring.boot.security.oauth2.OAuth2Properties;

/**
 * 客户端凭据资源详情
 *
 * @author gyl
 * @since 1.0.0
 */
@Slf4j
public class CloudClientCredentialsResourceDetails extends ClientCredentialsResourceDetails {

  private final LoadBalancerClientHelper loadBalancerClientHelper;

  @Getter @Setter private final String accessTokenUriServiceId;

  public CloudClientCredentialsResourceDetails(
      OAuth2Properties oAuth2Properties, LoadBalancerClientHelper loadBalancerClientHelper) {
    this.accessTokenUriServiceId = oAuth2Properties.getAuthorization().getAccessTokenUriServiceId();
    this.loadBalancerClientHelper = loadBalancerClientHelper;
  }

  @Override
  public String getAccessTokenUri() {
    String accessTokenUri = super.getAccessTokenUri();
    if (StringUtils.isNotBlank(accessTokenUriServiceId)) {
      return loadBalancerClientHelper.reconstructURI(accessTokenUriServiceId, accessTokenUri);
    }
    return accessTokenUri;
  }
}
