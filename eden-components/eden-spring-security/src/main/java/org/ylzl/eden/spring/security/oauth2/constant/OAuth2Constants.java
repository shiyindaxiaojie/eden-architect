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

package org.ylzl.eden.spring.security.oauth2.constant;

import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;

/**
 * OAuth2 常量定义
 *
 * @author gyl
 * @since 1.0.0
 */
@UtilityClass
public final class OAuth2Constants {

  // OAuth2 授权模式

  /** 授权码模式 */
  public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";

  /** 客户端凭据模式 */
  public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

  /** 隐式模式 */
  public static final String GRANT_TYPE_IMPLICIT = "implicit";

  /** 密码模式 */
  public static final String GRANT_TYPE_PASSWORD = "password";

  // OAuth2 端点

  public static final String ENDPOINT_AUTHORIZATION = "/oauth/authorize";

  public static final String ENDPOINT_CHECK_TOKEN = "/oauth/check_token";

  public static final String ENDPOINT_TOKEN = "/oauth/token";

  public static final String ENDPOINT_TOKEN_KEY = "/oauth/token_key";

  public static final String ENDPOINT_WHITELABEL_APPROVAL = "/oauth/confirm_access";

  public static final String ENDPOINT_WHITELABEL_ERROR = "/oauth/error";

  // OAuth2 端点（自定义）

  public static final String ENDPOINT_LOGIN = "/oauth/login";

  public static final String ENDPOINT_LOGOUT = "/oauth/logout";

  // OAuth2 令牌

  /** 访问令牌 */
  public static final String ACCESS_TOKEN = OAuth2AccessToken.ACCESS_TOKEN;

  /** 访问令牌的有效时长 */
  public static final String EXPIRES_IN = OAuth2AccessToken.EXPIRES_IN;

  /** OAuth2 类型 */
  public static final String OAUTH2_TYPE = OAuth2AccessToken.OAUTH2_TYPE;

  /** 刷新令牌 */
  public static final String REFRESH_TOKEN = OAuth2AccessToken.REFRESH_TOKEN;

  /** 令牌类型 */
  public static final String TOKEN_TYPE = OAuth2AccessToken.TOKEN_TYPE;

  /** 会话令牌 */
  public static final String SESSION_TOKEN = "session_token";

  public static final String AUD = AccessTokenConverter.AUD;

  public static final String EXP = AccessTokenConverter.EXP;

  public static final String JTI = AccessTokenConverter.JTI;

  public static final String ATI = AccessTokenConverter.ATI;

  public static final String ISSUED_AT = "iat";

  public static final String AUTHORITIES = AccessTokenConverter.AUTHORITIES;

  public static final String CLIENT_ID = OAuth2Utils.CLIENT_ID;

  public static final String STATE = OAuth2Utils.STATE;

  public static final String SCOPE = OAuth2Utils.SCOPE;

  public static final String REDIRECT_URI = OAuth2Utils.REDIRECT_URI;

  public static final String RESPONSE_TYPE = OAuth2Utils.RESPONSE_TYPE;

  public static final String USER_OAUTH_APPROVAL = OAuth2Utils.USER_OAUTH_APPROVAL;

  public static final String SCOPE_PREFIX = OAuth2Utils.SCOPE_PREFIX;

  public static final String GRANT_TYPE = OAuth2Utils.GRANT_TYPE;

  /** 用户 */
  public static final String USERNAME = "username";

  /** 密码 */
  public static final String PASSWORD = "password";

  /** 记住我 */
  public static final String REMEMBER_ME = "rememberMe";

  /** 用户属性名 */
  public static final String PRINCIPAL_ATTRIBUTE = "preferred_username";

  /** 权限属性名 */
  public static final String AUTHORITIES_ATTRIBUTE = "roles";

  // OAuth2 资源管理

  /** 开放标识 */
  public static final String CLIENT_SCOPE_OPENID = "openid";

  /** 网页客户端 */
  public static final String CLIENT_SCOPE_WEB = "webclient";

  /** 手机客户端 */
  public static final String CLIENT_SCOPE_MOBILE = "mobileclient";

  /** 允许访问 */
  public static final String ACCESS_PERMIT_ALL = "permitAll()";

  /** 需要认证 */
  public static final String ACCESS_IS_AUTHENTICATED = "isAuthenticated()";
}
