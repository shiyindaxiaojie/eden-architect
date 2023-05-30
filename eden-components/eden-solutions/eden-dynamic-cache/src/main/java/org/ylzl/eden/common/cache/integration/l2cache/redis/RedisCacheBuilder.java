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

package org.ylzl.eden.common.cache.integration.l2cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.ylzl.eden.common.cache.Cache;
import org.ylzl.eden.common.cache.builder.AbstractCacheBuilder;
import org.ylzl.eden.common.cache.builder.CacheBuilder;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * Redis 缓存构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class RedisCacheBuilder extends AbstractCacheBuilder {

	private static final String CACHE_CLIENT_USE_DEFAULT = "Redis client is null, use default client";

	private static final String CACHE_CLIENT_IS_NOT_REQUIRED_TYPE = "Redis client '{}' is not of required type RedissonClient";

	private volatile RedissonClient redissonClient;

	private static final Object lock = new Object();

	/**
	 * 设置二级缓存客户端
	 * <p>预留入口，支持外部注入Bean</p>
	 *
	 * @param l2CacheClient 二级缓存客户端
	 * @return CacheBuilder
	 */
	@Override
	public CacheBuilder l2CacheClient(Object l2CacheClient) {
		if (l2CacheClient == null) {
			log.warn(CACHE_CLIENT_USE_DEFAULT);
			return this;
		}
		if (!(l2CacheClient instanceof RedissonClient)) {
			throw new RuntimeException(MessageFormatUtils.format(CACHE_CLIENT_IS_NOT_REQUIRED_TYPE, l2CacheClient));
		}
		this.redissonClient = (RedissonClient) l2CacheClient;
		return this;
	}

	/**
	 * 构建 Cache 实例
	 *
	 * @return Cache 实例
	 */
	@Override
	public Cache build() {
		if (redissonClient == null) {
			synchronized (lock) {
				if (redissonClient == null) {
					redissonClient = Redisson.create(this.getCacheConfig().getL2Cache().getRedis().getConfig());
				}
			}
		}
		return new RedisCache(this.getCacheName(), this.getCacheConfig(), redissonClient);
	}
}
