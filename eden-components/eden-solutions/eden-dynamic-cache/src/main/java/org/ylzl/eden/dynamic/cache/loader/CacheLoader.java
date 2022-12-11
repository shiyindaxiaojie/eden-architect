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

import org.ylzl.eden.dynamic.cache.config.CacheSpec;
import org.ylzl.eden.dynamic.cache.L2Cache;
import org.ylzl.eden.dynamic.cache.consistency.CacheSynchronizer;
import org.ylzl.eden.extension.SPI;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * 缓存加载器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@SPI
public interface CacheLoader<K, V> extends Serializable {

	/**
	 * 根据 Key 加载 Value
	 *
	 * @param key 缓存Key
	 * @return Value
	 */
	V load(K key);

	/**
	 * 添加 ValueLoader
	 *
	 * @param key 缓存Key
	 * @param valueLoader ValueLoader 实例
	 */
	void addValueLoader(Object key, Callable<?> valueLoader);

	/**
	 * 移除 ValueLoader
	 *
	 * @param key 缓存Key
	 */
	void removeValueLoader(Object key);

	/**
	 * 设置二级缓存
	 *
	 * @param l2Cache 二级缓存实例
	 */
	void setL2Cache(L2Cache l2Cache);

	/**
	 * 设置缓存共享配置
	 *
	 * @param cacheSpec 缓存共享配置
	 */
	void setCacheSpec(CacheSpec cacheSpec);

	/**
	 * 设置缓存同步器
	 *
	 * @param cacheSynchronizer 缓存同步器
	 */
	void setCacheSynchronizer(CacheSynchronizer cacheSynchronizer);

	/**
	 * 设置是否允许空值
	 *
	 * @param allowNullValues 是否允许空值
	 */
	void setAllowNullValues(boolean allowNullValues);
}
