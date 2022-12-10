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

package org.ylzl.eden.dynamic.cache.loader;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.dynamic.cache.level.L2Cache;
import org.ylzl.eden.dynamic.cache.sync.CacheSynchronizer;

import java.util.function.Function;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class LoadFunction implements Function<Object, Object> {

	private final String instanceId;

	private final String cacheType;

	private final String cacheName;

	private final L2Cache l2Cache;

	private final CacheSynchronizer cacheSynchronizer;

	private final ValueLoaderWrapper valueLoader;

	private final boolean allowNullValues;

	private Cache<Object, Integer> nullValueCache;

	@Override
	public Object apply(Object key) {
		return null;
	}
}
