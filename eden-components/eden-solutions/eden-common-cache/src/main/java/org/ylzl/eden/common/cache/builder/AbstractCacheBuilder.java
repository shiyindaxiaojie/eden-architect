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

package org.ylzl.eden.common.cache.builder;

import org.ylzl.eden.common.cache.Cache;
import org.ylzl.eden.common.cache.config.CacheConfig;
import org.ylzl.eden.common.cache.l1cache.L1CacheLoader;
import org.ylzl.eden.common.cache.l1cache.L1CacheRemovalListener;
import org.ylzl.eden.extension.ExtensionLoader;

/**
 * 缓存构建器抽象
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class AbstractCacheBuilder implements CacheBuilder {

	private String cacheName;

	private CacheConfig cacheConfig;

	private L1CacheRemovalListener removalListener;

	/**
	 * 设置缓存名称
	 *
	 * @param cacheName 缓存名称
	 * @return CacheBuilder
	 */
	@Override
	public CacheBuilder cacheName(String cacheName) {
		this.cacheName = cacheName;
		return this;
	}

	/**
	 * 设置缓存配置
	 *
	 * @param cacheConfig 缓存配置
	 * @return CacheBuilder
	 */
	@Override
	public CacheBuilder cacheConfig(CacheConfig cacheConfig) {
		this.cacheConfig = cacheConfig;
		return this;
	}

	/**
	 * 设置缓存失效监听器
	 *
	 * @param removalListener 缓存失效监听器
	 * @return CacheBuilder
	 */
	@Override
	public CacheBuilder l1CacheRemovalListener(L1CacheRemovalListener removalListener) {
		this.removalListener = removalListener;
		return this;
	}

	/**
	 * 设置二级缓存客户端
	 *
	 * @param l2CacheClient 二级缓存客户端
	 * @return CacheBuilder
	 */
	@Override
	public CacheBuilder l2CacheClient(Object l2CacheClient) {
		return this;
	}

	/**
	 * 构建一级缓存实例
	 *
	 * @param l1CacheLoader 缓存加载器
	 * @return Cache 实例
	 */
	@Override
	public Cache build(L1CacheLoader l1CacheLoader) {
		// 二级缓存不需要执行 CacheLoader
		return build();
	}

	/**
	 * 获取缓存名称
	 *
	 * @return 缓存名称
	 */
	public String getCacheName() {
		return cacheName;
	}

	/**
	 * 获取缓存配置
	 *
	 * @return 缓存配置
	 */
	public CacheConfig getCacheConfig() {
		return cacheConfig;
	}

	/**
	 * 获取缓存失效监听器
	 *
	 * @return 缓存失效监听器
	 */
	public L1CacheRemovalListener getRemovalListener() {
		if (removalListener == null) {
			return ExtensionLoader.getExtensionLoader(L1CacheRemovalListener.class).getDefaultExtension();
		}
		return removalListener;
	}
}
