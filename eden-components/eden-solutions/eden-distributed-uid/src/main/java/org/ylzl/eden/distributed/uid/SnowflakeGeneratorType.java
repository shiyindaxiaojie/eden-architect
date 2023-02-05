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

package org.ylzl.eden.distributed.uid;

import lombok.Getter;

/**
 * 雪花算法生成器类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum SnowflakeGeneratorType {

	LEAF,
	UID_GENERATOR,
	TINY_ID;

	public static SnowflakeGeneratorType parse(String type) {
		for (SnowflakeGeneratorType snowflakeGeneratorType : SnowflakeGeneratorType.values()) {
			if (snowflakeGeneratorType.name().equalsIgnoreCase(type)) {
				return snowflakeGeneratorType;
			}
		}
		return null;
	}
}
