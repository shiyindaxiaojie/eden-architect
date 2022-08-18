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

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Zuul 配置属性
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ZuulProperties.PREFIX)
public class ZuulProperties {

	public static final String PREFIX = "spring.zuul";

	private final AccessControl accessControl = new AccessControl();

	private final RateLimiting rateLimiting = new RateLimiting();

	@Getter
	@Setter
	public static class AccessControl {

		private Map<String, List<String>> authorizedMicroservicesEndpoints = Collections.emptyMap();

		private boolean enabled = true;
	}

	@Getter
	@Setter
	public static class RateLimiting {

		private String defaultIncludePattern = null;

		private int durationInSeconds = 3_600;

		private boolean enabled = true;

		private long limit = 100_000L;
	}
}
