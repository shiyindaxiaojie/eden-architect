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

package org.ylzl.eden.spring.framework.core.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;
import org.ylzl.eden.spring.framework.core.constant.GlobalConstants;

/**
 * 框架配置属性
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
@Setter
@ConfigurationProperties(prefix = GlobalConstants.PROP_EDEN_PREFIX)
public class SpringFrameworkProperties {

	private final CorsConfiguration cors = new CorsConfiguration();

	private final Http http = new Http();

	@Getter
	@Setter
	public static class Http {

		private final Cache cache = new Cache();
		public Version version = SpringFrameworkDefaults.Http.version;
		private boolean useUndertowUserCipherSuitesOrder =
			SpringFrameworkDefaults.Http.useUndertowUserCipherSuitesOrder;

		public enum Version {
			V_1_1,
			V_2_0;
		}

		@Getter
		@Setter
		public static class Cache {

			private int timeToLiveInDays = SpringFrameworkDefaults.Http.Cache.timeToLiveInDays;
		}
	}
}
