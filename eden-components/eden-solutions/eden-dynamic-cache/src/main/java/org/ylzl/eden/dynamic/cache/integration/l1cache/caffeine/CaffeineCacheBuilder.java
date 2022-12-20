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
import org.jetbrains.annotations.NotNull;
import org.ylzl.eden.dynamic.cache.Cache;
import org.ylzl.eden.dynamic.cache.builder.AbstractCacheBuilder;
import org.ylzl.eden.dynamic.cache.expire.CacheRemovalCause;
import org.ylzl.eden.dynamic.cache.loader.CacheLoader;

/**
 * Caffeine 缓存构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class CaffeineCacheBuilder extends AbstractCacheBuilder {

	/**
	 * 构建 Cache 实例
	 *
	 * @return Cache 实例
	 */
	@Override
	public Cache build() {
		com.github.benmanes.caffeine.cache.Cache<Object, Object> cache = newCaffeineBuilder().build();
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
		com.github.benmanes.caffeine.cache.LoadingCache<Object, Object> cache = newCaffeineBuilder().build(cacheLoader::load);
		return new CaffeineCache(this.getCacheName(), this.getCacheConfig(), cache);
	}

	/**
	 * 构建 CaffeineBuilder
	 *
	 * @return Caffeine
	 */
	@NotNull
	private Caffeine<Object, Object> newCaffeineBuilder() {
		return Caffeine.newBuilder()
			.initialCapacity(this.getCacheConfig().getL1Cache().getInitialCapacity())
			.maximumSize(this.getCacheConfig().getL1Cache().getMaximumSize())
			.evictionListener((key, value, cause) -> {
				this.getRemovalListener().onRemoval(key, value, CacheRemovalCause.parse(cause.name()));
			});
	}
}
