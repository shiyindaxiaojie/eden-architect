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

import lombok.experimental.UtilityClass;

/**
 * OAuth2 配置属性默认值
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public final class OAuth2Defaults {

	public static class Authentication {
	}

	public static class Authorization {

		public static final String header = "Authorization";

		public static final String cookieDomain = null;

		public static final String accessTokenUri = "http://uaa/oauth/token";

		public static final String accessTokenUriServiceId = "uaa";

		public static final String publicTokenKeyUri = "http://uaa/oauth/token_key";

		public static final String publicTokenKeyUriServiceId = "uaa";

		public static final Long publicTokenKeyRefreshRateLimit = 10 * 1000L;

		public static final Long publicTokenKeyTtl = 24 * 60 * 60 * 1000L;

		public static final Integer sessionTimeoutSeconds = 1800;

		public static class Client {

			public static final Boolean enabled = false;
		}

		public static class Server {

			public static final Boolean enabled = false;
		}

		public static class ClientCredentials {

			public static final String clientId = null;

			public static final String clientSecret = null;

			public static final Integer accessTokenValiditySeconds = 5 * 60;

			public static final String[] scopes = {"client"};
		}

		public static class Code {

			public static final String clientId = null;

			public static final String clientSecret = null;

			public static final Integer accessTokenValiditySeconds = 5 * 60;

			public static final Integer refreshTokenValiditySeconds = 7 * 24 * 60 * 60;

			public static final String[] scopes = {"code"};

			public static final String[] registeredRedirectUris = null;
		}

		public static class Implicit {

			public static final String clientId = null;

			public static final String clientSecret = null;

			public static final Integer accessTokenValiditySeconds = 5 * 60;

			public static final String[] scopes = {"implicit"};
		}

		public static class Password {

			public static final String clientId = null;

			public static final String clientSecret = null;

			public static final Integer accessTokenValiditySeconds = 5 * 60;

			public static final Integer refreshTokenValiditySeconds = 7 * 24 * 60 * 60;

			public static final String[] scopes = {"password"};
		}

		public static class RefreshToken {

			public static final String clientId = null;

			public static final String clientSecret = null;
		}
	}

	public static class Resource {

		public static class Server {

			public static final Boolean enabled = false;
		}
	}

	public static class KeyStore {

		public static final String alias = "selfsigned";

		public static final String name = "keystore.jks";

		public static final String password = "guoyuanlu";
	}
}
