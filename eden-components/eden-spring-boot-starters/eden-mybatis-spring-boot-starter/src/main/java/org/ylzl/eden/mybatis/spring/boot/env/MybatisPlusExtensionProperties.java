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

package org.ylzl.eden.mybatis.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MybatisPlus 扩展配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = MybatisPlusExtensionProperties.PREFIX)
public class MybatisPlusExtensionProperties {

	public static final String PREFIX = "mybatis-plus.extension";

	public static final String AUTO_FILL_ENABLED = PREFIX + ".auto-fill.enabled";

	private final AutoFill autoFill = new AutoFill();

	@Data
	public static class AutoFill {

		private boolean enabled = true;

		private String createdDateFieldName = "created_date";

		private String lastModifiedDateFieldName = "last_modified_date";
	}
}
