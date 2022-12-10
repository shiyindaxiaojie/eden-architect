/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.sentinel.spring.cloud.autoconfigure;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.cloud.sentinel.web.CustomBlockExceptionHandler;

/**
 * Sentinel 防护自定义异常自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnClass(BlockExceptionHandler.class)
@ConditionalOnProperty(name = "spring.cloud.sentinel.enabled", matchIfMissing = true)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SentinelBlockAutoConfiguration {

	public static final String AUTOWIRED_CUSTOM_BLOCK_EXCEPTION_HANDLER = "Autowired CustomBlockExceptionHandler";

	@Bean
	public CustomBlockExceptionHandler customBlockExceptionHandler() {
		log.debug(AUTOWIRED_CUSTOM_BLOCK_EXCEPTION_HANDLER);
		return new CustomBlockExceptionHandler();
	}
}
