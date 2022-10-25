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

package org.ylzl.eden.commons.collections;

import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.ArrayUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 集合工具集
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public final class CollectionUtils {

	public static boolean isEmpty(final Collection<?> coll) {
		return org.apache.commons.collections4.CollectionUtils.isEmpty(coll);
	}

	public static boolean isNotEmpty(final Collection<?> coll) {
		return org.apache.commons.collections4.CollectionUtils.isNotEmpty(coll);
	}

	public static boolean isEmpty(final Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static boolean isNotEmpty(final Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static Map<String, String> toStringMap(String... pairs) {
		Map<String, String> parameters = new HashMap<>();
		if (ArrayUtils.isEmpty(pairs)) {
			return parameters;
		}

		if (pairs.length > 0) {
			if (pairs.length % 2 != 0) {
				throw new IllegalArgumentException("pairs must be even.");
			}
			for (int i = 0; i < pairs.length; i = i + 2) {
				parameters.put(pairs[i], pairs[i + 1]);
			}
		}
		return parameters;
	}

	public static <K, V> Map<K, V> toMap(Object... pairs) {
		Map<K, V> ret = new HashMap<>();
		if (pairs == null || pairs.length == 0) {
			return ret;
		}

		if (pairs.length % 2 != 0) {
			throw new IllegalArgumentException("Map pairs can not be odd number.");
		}
		int len = pairs.length / 2;
		for (int i = 0; i < len; i++) {
			ret.put((K) pairs[2 * i], (V) pairs[2 * i + 1]);
		}
		return ret;
	}
}
