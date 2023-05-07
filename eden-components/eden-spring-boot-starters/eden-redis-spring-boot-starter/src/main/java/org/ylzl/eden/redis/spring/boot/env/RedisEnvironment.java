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

package org.ylzl.eden.redis.spring.boot.env;

import lombok.experimental.UtilityClass;

/**
 * Redis 环境变量
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class RedisEnvironment {

	public static final String ENABLED = "spring.redis.enabled";

	public static final String HOST = "spring.redis.host";

	public static final String PORT = "spring.redis.port";

	public static final String URL = "spring.redis.url";

	public static final String DATABASE = "spring.redis.database";
}
