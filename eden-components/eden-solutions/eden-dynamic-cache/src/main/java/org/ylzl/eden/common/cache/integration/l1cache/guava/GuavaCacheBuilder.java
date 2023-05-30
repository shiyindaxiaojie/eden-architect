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

package org.ylzl.eden.common.cache.integration.l1cache.guava;

import com.google.common.cache.CacheBuilder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.ylzl.eden.common.cache.Cache;
import org.ylzl.eden.common.cache.builder.AbstractCacheBuilder;
import org.ylzl.eden.common.cache.l1cache.L1CacheLoader;
import org.ylzl.eden.common.cache.l1cache.L1CacheRemovalCause;

/**
 * Guava 缓存构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class GuavaCacheBuilder extends AbstractCacheBuilder {

	/**
	 * 构建 Cache 实例
	 *
	 * @return Cache 实例
	 */
	@Override
	public Cache build() {
		com.google.common.cache.Cache<Object, Object> cache = newGuavaBuilder().build();
		return new GuavaCache(this.getCacheName(), this.getCacheConfig(), cache);
	}

	/**
	 * 构建 Cache 实例
	 *
	 * @param l1CacheLoader 缓存加载器
	 * @return Cache 实例
	 */
	@Override
	public Cache build(L1CacheLoader l1CacheLoader) {
		com.google.common.cache.LoadingCache<Object, Object> cache = newGuavaBuilder().build(
			new com.google.common.cache.CacheLoader<Object, Object>() {

				@Override
				public Object load(@NonNull Object key) {
					return l1CacheLoader.load(key);
				}
			});
		return new GuavaCache(this.getCacheName(), this.getCacheConfig(), cache);
	}

	/**
	 * 构建 Guava
	 *
	 * @return Guava
	 */
	@NotNull
	private CacheBuilder<Object, Object> newGuavaBuilder() {
		return CacheBuilder.newBuilder()
			.initialCapacity(this.getCacheConfig().getL1Cache().getInitialCapacity())
			.maximumSize(this.getCacheConfig().getL1Cache().getMaximumSize())
			.removalListener(notification -> {
				this.getRemovalListener().onRemoval(notification.getKey(), notification.getValue(),
					L1CacheRemovalCause.parse(notification.getCause().name()));
			});
	}
}
