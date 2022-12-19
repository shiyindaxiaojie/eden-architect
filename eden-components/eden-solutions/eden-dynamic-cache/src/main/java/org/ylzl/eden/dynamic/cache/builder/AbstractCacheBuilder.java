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

import lombok.Getter;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.expire.CacheRemovalListener;

/**
 * 缓存构造器抽象
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public abstract class AbstractCacheBuilder implements CacheBuilder {

	private String cacheName;

	private CacheConfig cacheConfig;

	private int initialCapacity;

	private long maximumSize;

	private CacheRemovalListener listener;

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
	 * 设置初始化容量
	 *
	 * @param initialCapacity 初始化容量
	 * @return CacheBuilder
	 */
	@Override
	public CacheBuilder initialCapacity(int initialCapacity) {
		this.initialCapacity = initialCapacity;
		return this;
	}

	/**
	 * 设置最大容量
	 *
	 * @param maximumSize 最大容量
	 * @return CacheBuilder
	 */
	@Override
	public CacheBuilder maximumSize(long maximumSize) {
		this.maximumSize = maximumSize;
		return this;
	}

	/**
	 * 设置缓存失效监听器
	 *
	 * @param listener 缓存失效监听器
	 * @return CacheBuilder
	 */
	@Override
	public CacheBuilder evictionListener(CacheRemovalListener listener) {
		this.listener = listener;
		return this;
	}
}
