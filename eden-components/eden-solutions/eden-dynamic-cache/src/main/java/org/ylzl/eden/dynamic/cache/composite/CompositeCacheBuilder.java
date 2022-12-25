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

package org.ylzl.eden.dynamic.cache.composite;

import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.dynamic.cache.Cache;
import org.ylzl.eden.dynamic.cache.L1Cache;
import org.ylzl.eden.dynamic.cache.L2Cache;
import org.ylzl.eden.dynamic.cache.builder.AbstractCacheBuilder;
import org.ylzl.eden.dynamic.cache.builder.CacheBuilder;
import org.ylzl.eden.extension.ExtensionLoader;

/**
 * Redis 缓存构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class CompositeCacheBuilder extends AbstractCacheBuilder {

	private Object l2CacheClient;

	/**
	 * 设置二级缓存客户端
	 * <p>预留入口，支持外部注入Bean</p>
	 *
	 * @param l2CacheClient 二级缓存客户端
	 * @return CacheBuilder
	 */
	@Override
	public CacheBuilder l2CacheClient(Object l2CacheClient) {
		this.l2CacheClient = l2CacheClient;
		return this;
	}

	/**
	 * 构建 Cache 实例
	 *
	 * @return Cache 实例
	 */
	@Override
	public Cache build() {
		String l1CacheType = this.getCacheConfig().getComposite().getL1CacheType();
		L1Cache l1Cache = (L1Cache) newCacheBuilder(l1CacheType).build();

		String l2CacheType = this.getCacheConfig().getComposite().getL2CacheType();
		CacheBuilder cacheBuilder = newCacheBuilder(l2CacheType);
		cacheBuilder.l2CacheClient(this.l2CacheClient);
		L2Cache l2Cache = (L2Cache) cacheBuilder.build();
		return new CompositeCache(this.getCacheName(), this.getCacheConfig(), l1Cache, l2Cache);
	}

	/**
	 * 获取 CacheBuilder
	 *
	 * @param cacheType 缓存类型
	 * @return CacheBuilder
	 */
	private CacheBuilder newCacheBuilder(String cacheType) {
		return ExtensionLoader.getExtensionLoader(CacheBuilder.class).getExtension(cacheType);
	}
}
