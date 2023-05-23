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

package org.ylzl.eden.liquibase.spring.boot.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 扩展 LiquibaseProperties
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Setter
@Getter
@ConfigurationProperties(prefix = ExtendLiquibaseProperties.PREFIX)
public class ExtendLiquibaseProperties {

	/**
	 * 官方的 LiquibaseProperties 加了 {@code ignoreUnknownFields = false}，很坑
	 */
	public static final String PREFIX = "liquibase";

	private boolean async;
}
