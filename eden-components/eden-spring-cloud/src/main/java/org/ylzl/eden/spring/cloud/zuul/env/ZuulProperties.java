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
package org.ylzl.eden.spring.cloud.zuul.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.cloud.core.constant.SpringCloudConstants;

import java.util.List;
import java.util.Map;

/**
 * Zuul 配置属性
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SpringCloudConstants.PROP_PREFIX + ".zuul")
public class ZuulProperties {

	private final AccessControl accessControl = new AccessControl();

	private final RateLimiting rateLimiting = new RateLimiting();

	@Getter
	@Setter
	public static class AccessControl {

		private Map<String, List<String>> authorizedMicroservicesEndpoints =
			ZuulDefaults.AccessControl.authorizedMicroservicesEndpoints;

		private boolean enabled = ZuulDefaults.AccessControl.enabled;
	}

	@Getter
	@Setter
	public static class RateLimiting {

		private String defaultIncludePattern = ZuulDefaults.RateLimiting.defaultIncludePattern;

		private int durationInSeconds = ZuulDefaults.RateLimiting.durationInSeconds;

		private boolean enabled = ZuulDefaults.RateLimiting.enabled;

		private long limit = ZuulDefaults.RateLimiting.limit;
	}
}
