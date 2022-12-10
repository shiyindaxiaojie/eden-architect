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

package org.ylzl.redisson.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redisson 配置补充
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConfigurationProperties(prefix = FixedRedissonProperties.PREFIX)
@Data
public class FixedRedissonProperties {

	public static final String PREFIX = "spring.redis.redisson";

	private int timeout;

	private int connectionPoolSize;

	private int connectionMinimumIdleSize;
}
