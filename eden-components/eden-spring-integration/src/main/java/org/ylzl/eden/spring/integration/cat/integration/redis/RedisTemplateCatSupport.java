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

package org.ylzl.eden.spring.integration.cat.integration.redis;

import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.integration.cat.CatConstants;
import org.ylzl.eden.spring.integration.cat.CatClient;
import org.ylzl.eden.spring.integration.cat.config.CatState;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;

/**
 * Redis 支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class RedisTemplateCatSupport {

	private static final int MAX_KEY_LEN = 10;

	private static final String KEY = "key";

	private static final String KEYS = "keys";

	public static void execute(String command, Runnable runnable) {
		if (!CatState.isInitialized()) {
			runnable.run();
			return;
		}

		Map<String, Object> data = initData(1);
		CatClient.newTransaction(CatConstants.TYPE_CACHE, command, data, runnable);
	}

	public static void execute(String command, byte[] key, Runnable runnable) {
		if (!CatState.isInitialized()) {
			runnable.run();
			return;
		}

		Map<String, Object> data = initData(2);
		data.put(KEY, deserialize(key));
		CatClient.newTransaction(CatConstants.TYPE_CACHE, command, data, runnable);
	}

	public static void execute(String command, byte[][] keys, Runnable runnable) {
		if (!CatState.isInitialized()) {
			runnable.run();
			return;
		}

		Map<String, Object> data = initData(2);
		data.put(KEYS, toStringWithDeserialization(limitKeys(keys)));
		CatClient.newTransaction(CatConstants.TYPE_CACHE, command, data, runnable);
	}

	public static <T> T execute(String command, byte[] key, Supplier<T> supplier) {
		if (!CatState.isInitialized()) {
			return supplier.get();
		}

		Map<String, Object> data = initData(2);
		data.put(KEY, deserialize(key));
		return CatClient.newTransaction(CatConstants.TYPE_CACHE, command, data, supplier);
	}

	public static <T> T execute(String command, Supplier<T> supplier) {
		if (!CatState.isInitialized()) {
			return supplier.get();
		}

		Map<String, Object> data = initData(1);
		return CatClient.newTransaction(CatConstants.TYPE_CACHE, command, data, supplier);
	}

	public static <T> T execute(String command, byte[][] keys, Supplier<T> supplier) {
		if (!CatState.isInitialized()) {
			return supplier.get();
		}

		Map<String, Object> data = initData(2);
		data.put(KEYS, toStringWithDeserialization(limitKeys(keys)));
		return CatClient.newTransaction(CatConstants.TYPE_CACHE, command, data, supplier);
	}

	private static Map<String, Object> initData(int initialCapacity) {
		Map<String, Object> data = new HashMap<>(initialCapacity);
		data.put(CatConstants.DATA_COMPONENT, CatConstants.DATA_COMPONENT_REDIS);
		return data;
	}

	private static String deserialize(byte[] bytes) {
		return (bytes == null ? Strings.EMPTY : new String(bytes, StandardCharsets.UTF_8));
	}

	private static String toStringWithDeserialization(byte[][] array) {
		if (array == null) {
			return Strings.EMPTY;
		}

		List<String> list = new ArrayList<>();
		for (byte[] bytes : array) {
			list.add(deserialize(bytes));
		}

		return "[" + String.join(", ", list) + "]";
	}

	private static <T> T[] limitKeys(T[] keys) {
		if (keys != null && keys.length > MAX_KEY_LEN) {
			return Arrays.copyOfRange(keys, 0, MAX_KEY_LEN);
		}
		return keys;
	}
}
