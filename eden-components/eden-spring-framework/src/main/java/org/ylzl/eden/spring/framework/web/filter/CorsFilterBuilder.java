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

package org.ylzl.eden.spring.framework.web.filter;

import lombok.NoArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * 跨域过滤器构造工具
 *
 * @author gyl
 * @since 2.4.x
 */
public class CorsFilterBuilder {

	public static CorsFilterBuilder.Builder builder() {
		return new CorsFilterBuilder.Builder();
	}

	@NoArgsConstructor
	public static class Builder {

		private List<String> paths;

		private CorsConfiguration corsConfiguration;

		public CorsFilterBuilder.Builder paths(List<String> paths) {
			this.paths = paths;
			return this;
		}

		public CorsFilterBuilder.Builder corsConfiguration(CorsConfiguration corsConfiguration) {
			this.corsConfiguration = corsConfiguration;
			return this;
		}

		public CorsFilter build() {
			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			for (String path : paths) {
				source.registerCorsConfiguration(path, corsConfiguration);
			}
			return new CorsFilter(source);
		}
	}
}
