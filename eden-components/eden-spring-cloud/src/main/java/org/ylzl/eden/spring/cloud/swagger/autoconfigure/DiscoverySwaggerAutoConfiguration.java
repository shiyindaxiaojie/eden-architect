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

package org.ylzl.eden.spring.cloud.swagger.autoconfigure;

import com.netflix.zuul.ZuulFilter;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.cloud.swagger.filter.SwaggerBasePathRewritingFilter;

/**
 * 基于 Zuul 的 Swagger 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnClass({ZuulFilter.class, Swagger.class})
@ConditionalOnWebApplication
@Slf4j
@Configuration
public class DiscoverySwaggerAutoConfiguration {

	private static final String MSG_AUTOWIRED_BASE_PATH_FILTER = "Autowired Zuul Swagger filter";

	@ConditionalOnMissingBean
	@Bean
	public SwaggerBasePathRewritingFilter swaggerBasePathRewritingFilter() {
		log.debug(MSG_AUTOWIRED_BASE_PATH_FILTER);
		return new SwaggerBasePathRewritingFilter();
	}
}
