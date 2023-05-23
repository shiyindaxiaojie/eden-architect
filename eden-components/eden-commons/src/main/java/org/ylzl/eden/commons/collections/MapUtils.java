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

package org.ylzl.eden.commons.collections;

import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * Map 工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see org.apache.commons.collections4.MapUtils
 * @since 2.4.13
 */
@UtilityClass
public class MapUtils {

	public static boolean isEmpty(final Map<?, ?> map) {
		return org.apache.commons.collections4.MapUtils.isEmpty(map);
	}

	public static boolean isNotEmpty(final Map<?, ?> map) {
		return org.apache.commons.collections4.MapUtils.isNotEmpty(map);
	}
}
