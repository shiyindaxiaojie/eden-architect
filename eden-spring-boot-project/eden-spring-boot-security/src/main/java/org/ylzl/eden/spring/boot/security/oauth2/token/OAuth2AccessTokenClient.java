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

package org.ylzl.eden.spring.boot.security.oauth2.token;

import org.springframework.http.HttpEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.MultiValueMap;

/**
 * OAuth2 访问令牌客户端
 *
 * @author gyl
 * @since 0.0.1
 */
public interface OAuth2AccessTokenClient {

  /**
   * 获取 OAuth2 访问令牌
   *
   * @param entity 请求实体
   * @return OAuth2 访问令牌
   */
  OAuth2AccessToken getOAuth2AccessToken(HttpEntity<MultiValueMap<String, String>> entity);
}
