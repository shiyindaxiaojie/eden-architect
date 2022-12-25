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

package org.ylzl.eden.dynamic.cache.l1cache;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
public class AsyncL1CacheRemovalListener {

	private final L1CacheRemovalListener delegate;

	private final Executor executor;

	/**
	 * 缓存过期触发
	 *
	 * @param key   Key
	 * @param future CompletableFuture
	 * @param cause 缓存失效原因
	 */
	public <K, V> void onRemoval(K key, CompletableFuture<V> future, L1CacheRemovalCause cause) {
		if (future != null) {
			future.thenAcceptAsync(value -> {
				if (value != null) {
					delegate.onRemoval(key, value, cause);
				}
			}, executor);
		}
	}
}
