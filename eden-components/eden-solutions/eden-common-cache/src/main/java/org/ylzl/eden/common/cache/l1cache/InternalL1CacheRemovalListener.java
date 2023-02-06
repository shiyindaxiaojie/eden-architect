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

package org.ylzl.eden.common.cache.l1cache;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.common.cache.Cache;
import org.ylzl.eden.common.cache.composite.CompositeCache;
import org.ylzl.eden.common.cache.support.value.NullValue;
import org.ylzl.eden.common.cache.L2Cache;

/**
 * 内置一级缓存失效监听器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
public class InternalL1CacheRemovalListener implements L1CacheRemovalListener {

	private Cache cache;

	/**
	 * 缓存过期触发
	 *
	 * @param key   Key
	 * @param value Value
	 * @param cause 缓存失效原因
	 */
	@Override
	public <K, V> void onRemoval(K key, V value, L1CacheRemovalCause cause) {
		if (!(value instanceof NullValue) || cache == null) {
			return;
		}

		if (cache instanceof CompositeCache) {
			L2Cache l2Cache = ((CompositeCache) cache).getL2Cache();
			if (l2Cache == null) {
				return;
			}
			l2Cache.evict(key);
		}
	}

	/**
	 * 设置缓存实例
	 *
	 * @param cache 缓存实例
	 */
	@Override
	public void setL2Cache(@NonNull Cache cache) {
		this.cache = cache;
	}
}
