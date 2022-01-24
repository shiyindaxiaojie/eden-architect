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

package org.ylzl.eden.spring.security.oauth2.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.security.core.constant.SpringSecurityConstants;

/**
 * OAuth2 配置属性
 *
 * @author gyl
 * @since 2.4.x
 */
@SuppressWarnings("unchecked")
@Getter
@Setter
@ConfigurationProperties(prefix = SpringSecurityConstants.PROP_PREFIX + ".oauth2")
public class OAuth2Properties {

  private final Authentication authentication = new Authentication();

  private final Authorization authorization = new Authorization();

  private final KeyStore keyStore = new KeyStore();

  private final Resource resource = new Resource();

  @Getter
  @Setter
  public static class Authentication {}

  @Getter
  @Setter
  public static class Authorization {

    private final ClientCredentials clientCredentials = new ClientCredentials();
    private final Code code = new Code();
    private final Implicit implicit = new Implicit();
    private final Password password = new Password();
    private final RefreshToken refreshToken = new RefreshToken();
    private final Client Client = new Client();
    private final Server server = new Server();
    private String header = OAuth2Defaults.Authorization.header;
    ;
    private String accessTokenUri = OAuth2Defaults.Authorization.accessTokenUri;
    private String accessTokenUriServiceId = OAuth2Defaults.Authorization.accessTokenUriServiceId;
    private String publicTokenKeyUri = OAuth2Defaults.Authorization.publicTokenKeyUri;
    private String publicTokenKeyUriServiceId =
        OAuth2Defaults.Authorization.publicTokenKeyUriServiceId;
    private Long publicTokenKeyRefreshRateLimit =
        OAuth2Defaults.Authorization.publicTokenKeyRefreshRateLimit;
    private Long publicTokenKeyTtl = OAuth2Defaults.Authorization.publicTokenKeyTtl;
    private String cookieDomain = OAuth2Defaults.Authorization.cookieDomain;
    private Integer sessionTimeoutSeconds = OAuth2Defaults.Authorization.sessionTimeoutSeconds;

    public enum TokenStore {
      IN_MEMORY,
      JDBC,
      JWK,
      JWT,
      REDIS;
    }

    @Getter
    @Setter
    public static class Client {

      private Boolean enabled = OAuth2Defaults.Authorization.Client.enabled;
    }

    @Getter
    @Setter
    public static class Server {

      private Boolean enabled = OAuth2Defaults.Authorization.Server.enabled;
    }

    @Getter
    @Setter
    public static class ClientCredentials {

      private String clientId = OAuth2Defaults.Authorization.ClientCredentials.clientId;

      private String clientSecret = OAuth2Defaults.Authorization.ClientCredentials.clientSecret;

      private Integer accessTokenValiditySeconds =
          OAuth2Defaults.Authorization.ClientCredentials.accessTokenValiditySeconds;

      private String[] scopes = OAuth2Defaults.Authorization.ClientCredentials.scopes;
    }

    @Getter
    @Setter
    public static class Code {

      private String clientId = OAuth2Defaults.Authorization.Code.clientId;

      private String clientSecret = OAuth2Defaults.Authorization.Code.clientSecret;

      private Integer accessTokenValiditySeconds =
          OAuth2Defaults.Authorization.Code.accessTokenValiditySeconds;

      private Integer refreshTokenValiditySeconds =
          OAuth2Defaults.Authorization.Code.refreshTokenValiditySeconds;

      private String[] scopes = OAuth2Defaults.Authorization.Code.scopes;

      private String[] registeredRedirectUris =
          OAuth2Defaults.Authorization.Code.registeredRedirectUris;
    }

    @Getter
    @Setter
    public static class Implicit {

      private String[] scopes = OAuth2Defaults.Authorization.Implicit.scopes;

      private Integer accessTokenValiditySeconds =
          OAuth2Defaults.Authorization.Implicit.accessTokenValiditySeconds;
    }

    @Getter
    @Setter
    public static class Password {

      private String clientId = OAuth2Defaults.Authorization.ClientCredentials.clientId;
      ;

      private String clientSecret = OAuth2Defaults.Authorization.ClientCredentials.clientSecret;
      ;

      private Integer accessTokenValiditySeconds =
          OAuth2Defaults.Authorization.Password.accessTokenValiditySeconds;

      private Integer refreshTokenValiditySeconds =
          OAuth2Defaults.Authorization.Password.refreshTokenValiditySeconds;

      private String[] scopes = OAuth2Defaults.Authorization.Password.scopes;
    }

    @Getter
    @Setter
    public static class RefreshToken {

      private String clientId = OAuth2Defaults.Authorization.RefreshToken.clientId;

      private String clientSecret = OAuth2Defaults.Authorization.RefreshToken.clientSecret;
    }
  }

  @Getter
  @Setter
  public static class Resource {

    private final Server server = new Server();

    @Getter
    @Setter
    public static class Server {

      private Boolean enabled = OAuth2Defaults.Resource.Server.enabled;
    }
  }

  @Getter
  @Setter
  public static class KeyStore {

    private String alias;

    private String name;

    private String password;
  }
}
