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

package org.ylzl.eden.dynamic.cache.integration.l1cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.dynamic.cache.Cache;
import org.ylzl.eden.dynamic.cache.builder.AbstractCacheBuilder;
import org.ylzl.eden.dynamic.cache.expire.CacheRemovalCause;
import org.ylzl.eden.dynamic.cache.loader.CacheLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Caffeine 缓存构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class CaffeineCacheBuilder extends AbstractCacheBuilder {

	private static final Map<String, CustomCaffeineSpec> caffeineSpecMap = new HashMap<>(16);

	/**
	 * 构建 Cache 实例
	 *
	 * @return Cache 实例
	 */
	@Override
	public Cache build() {
		com.github.benmanes.caffeine.cache.Cache<?, ?> cache = Caffeine.newBuilder()
				.initialCapacity(this.getInitialCapacity())
				.maximumSize(this.getMaximumSize())
				.evictionListener((key, value, cause) -> {
					this.getListener().onRemoval(key, value, CacheRemovalCause.parse(cause.name()));
				})
				.build();
		return new CaffeineCache(this.getCacheName(), this.getCacheConfig(), cache);
	}

	/**
	 * 构建 Cache 实例
	 *
	 * @param cacheLoader 缓存加载器
	 * @return Cache 实例
	 */
	@Override
	public Cache build(CacheLoader cacheLoader) {
		com.github.benmanes.caffeine.cache.LoadingCache<?, ?> cache = Caffeine.newBuilder()
				.initialCapacity(this.getInitialCapacity())
				.maximumSize(this.getMaximumSize())
				.evictionListener((key, value, cause) -> {
					this.getListener().onRemoval(key, value, CacheRemovalCause.parse(cause.name()));
				})
				.build((com.github.benmanes.caffeine.cache.CacheLoader) cacheLoader::load);
		return new CaffeineCache(this.getCacheName(), this.getCacheConfig(), cache);
	}
}
