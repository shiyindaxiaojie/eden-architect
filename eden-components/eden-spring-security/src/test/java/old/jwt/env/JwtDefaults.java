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

package old.jwt.env;

import lombok.experimental.UtilityClass;

/**
 * JWT 配置属性默认值
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public final class JwtDefaults {

	public static class Authentication {

		public static final String base64Secret = null;

		public static final String secret = null;

		public static final long tokenValidityInSeconds = 1800;

		public static final long tokenValidityInSecondsForRememberMe = 2592000;
	}

	public static class Authorization {

		public static final String header = "Authorization";

		public static class Server {

			public static final Boolean enabled = false;
		}
	}
}
