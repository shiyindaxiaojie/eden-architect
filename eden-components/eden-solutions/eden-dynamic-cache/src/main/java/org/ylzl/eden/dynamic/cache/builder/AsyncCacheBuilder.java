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

package org.ylzl.eden.dynamic.cache.builder;

import org.ylzl.eden.dynamic.cache.Cache;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.l1cache.L1CacheLoader;
import org.ylzl.eden.dynamic.cache.l1cache.L1CacheRemovalListener;
import org.ylzl.eden.extension.SPI;

/**
 * 缓存构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI
public interface AsyncCacheBuilder {

	/**
	 * 设置缓存名称
	 *
	 * @param cacheName 缓存名称
	 * @return CacheBuilder
	 */
	AsyncCacheBuilder cacheName(String cacheName);

	/**
	 * 设置缓存配置
	 *
	 * @param cacheConfig 缓存配置
	 * @return CacheBuilder
	 */
	AsyncCacheBuilder cacheConfig(CacheConfig cacheConfig);

	/**
	 * 设置缓存失效监听器
	 *
	 * @param removalListener 缓存失效监听器
	 * @return CacheBuilder
	 */
	AsyncCacheBuilder evictionListener(L1CacheRemovalListener removalListener);

	/**
	 * 构建 Cache 实例
	 *
	 * @return Cache 实例
	 */
	Cache buildAsync();

    /**
     * 构建 Cache 实例
	 *
	 * @param l1CacheLoader 缓存加载器
	 * @return Cache 实例
	 */
	Cache buildAsync(L1CacheLoader l1CacheLoader);
}
