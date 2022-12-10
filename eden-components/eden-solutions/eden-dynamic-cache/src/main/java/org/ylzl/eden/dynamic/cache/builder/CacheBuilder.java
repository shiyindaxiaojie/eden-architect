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

import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.config.CacheSpec;
import org.ylzl.eden.dynamic.cache.Cache;
import org.ylzl.eden.dynamic.cache.expire.CacheExpiredListener;
import org.ylzl.eden.dynamic.cache.sync.CacheSynchronizer;
import org.ylzl.eden.extension.SPI;

import java.io.Serializable;

/**
 * 缓存构造器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@SPI
public interface CacheBuilder<T extends Cache> extends Serializable {

	/**
	 * 构建指定名称的缓存对象
	 *
	 * @param cacheName
	 * @return
	 */
	T build(String cacheName);

	/**
	 * 解析指定名称的缓存配置
	 *
	 * @param cacheName
	 * @return
	 */
	CacheSpec parseSpec(String cacheName);

	/**
	 * 获取缓存配置
	 *
	 * @return
	 */
	CacheConfig getCacheConfig();

	/**
	 * 设置缓存配置
	 *
	 * @param cacheConfig
	 * @return
	 */
	CacheBuilder<T> setCacheConfig(CacheConfig cacheConfig);

	/**
	 * 获取缓存过期监听器
	 *
	 * @return
	 */
	CacheExpiredListener<Object, Object> getExpiredListener();

	/**
	 * 设置缓存过期监听器
	 *
	 * @param cacheExpiredListener
	 * @return
	 */
	CacheBuilder<T> setExpiredListener(CacheExpiredListener<Object, Object> cacheExpiredListener);

	/**
	 * 获取缓存同步器
	 *
	 * @return
	 */
	CacheSynchronizer getCacheSynchronizer();

	/**
	 * 设置缓存同步器
	 *
	 * @param cacheSynchronizer
	 * @return
	 */
	CacheBuilder<T> setCacheSynchronizer(CacheSynchronizer cacheSynchronizer);

	/**
	 * 获取实际执行的缓存客户端
	 *
	 * @return
	 */
	Object getCacheClient();

	/**
	 * 设置实际执行的缓存客户端
	 *
	 * @param cacheClient
	 * @return
	 */
	CacheBuilder<T> setCacheClient(Object cacheClient);
}
