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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Google ImmutableMap 工具集
 *
 * @author gyl
 * @since 2.4.x
 */
@SuppressWarnings("unchecked")
@UtilityClass
public class ImmutableMapUtils {

	private static final Function DEFAULT_FUNCTION =
		new Function() {

			@Override
			public Object apply(Object object) {
				return object;
			}
		};

	public static <T, K, V> HashMap<K, V> buildHashMap(
		@NonNull T[] objects, @NonNull Function<T, K> keyFn) {
		return buildHashMap(objects, keyFn, DEFAULT_FUNCTION);
	}

	public static <T, K, V> HashMap<K, V> buildHashMap(
		@NonNull T[] objects, @NonNull Function<T, K> keyFn, @NonNull Function<T, V> valueFn) {
		return buildHashMap(Arrays.asList(objects), keyFn, valueFn);
	}

	public static <T, K, V> HashMap<K, V> buildHashMap(
		@NonNull List<T> objects, @NonNull Function<T, K> keyFn) {
		return buildHashMap(objects, keyFn, DEFAULT_FUNCTION);
	}

	public static <T, K, V> HashMap<K, V> buildHashMap(
		@NonNull List<T> objects, @NonNull Function<T, K> keyFn, @NonNull Function<T, V> valueFn) {
		HashMap<K, V> hashMap = Maps.newHashMapWithExpectedSize(objects.size());
		for (T object : objects) {
			hashMap.put(keyFn.apply(object), valueFn.apply(object));
		}
		return hashMap;
	}

	public static <T, K, V> ImmutableMap<K, V> build(
		@NonNull T[] objects, @NonNull Function<T, K> keyFn)
		throws NoSuchMethodException, InstantiationException, IllegalAccessException,
		InvocationTargetException {
		return build(objects, keyFn, DEFAULT_FUNCTION);
	}

	public static <T, K, V> ImmutableMap<K, V> build(
		@NonNull T[] objects, @NonNull Function<T, K> keyFn, @NonNull Function<T, V> valueFn)
		throws NoSuchMethodException, InstantiationException, IllegalAccessException,
		InvocationTargetException {
		return build(Arrays.asList(objects), keyFn, valueFn);
	}

	public static <T, K, V> ImmutableMap<K, V> build(
		@NonNull List<T> objects, @NonNull Function<T, K> keyFn)
		throws NoSuchMethodException, InstantiationException, IllegalAccessException,
		InvocationTargetException {
		return build(objects, keyFn, DEFAULT_FUNCTION);
	}

	public static <T, K, V> ImmutableMap<K, V> build(
		@NonNull List<T> objects, @NonNull Function<T, K> keyFn, @NonNull Function<T, V> valueFn)
		throws InvocationTargetException, NoSuchMethodException, InstantiationException,
		IllegalAccessException {
		ImmutableMap.Builder<K, V> builder = builderWithExpectedSize(objects.size());
		for (T object : objects) {
			K key = Objects.requireNonNull(keyFn.apply(object));
			V value = Objects.requireNonNull(valueFn.apply(object));
			builder.put(key, value);
		}
		return builder.build();
	}

	private static <K, V> ImmutableMap.Builder<K, V> builderWithExpectedSize(int size)
		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
		InstantiationException {
		Constructor<ImmutableMap.Builder> constructor =
			ImmutableMap.Builder.class.getDeclaredConstructor(int.class);
		constructor.setAccessible(true);
		return constructor.newInstance(size);
	}
}
