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

package org.ylzl.eden.dynamic.cache;

/**
 * 缓存类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public enum CacheType {

	COMPOSITE(0),
	CAFFEINE(1),
	GUAVA(1),
	REDIS(2),
	DRAGONFLY(2),
	HAZELCAST(2);

	private final int level;

	CacheType(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public static CacheType parse(String type) {
		for (CacheType cacheType : CacheType.values()) {
			if (cacheType.name().equalsIgnoreCase(type)) {
				return cacheType;
			}
		}
		return null;
	}
}
