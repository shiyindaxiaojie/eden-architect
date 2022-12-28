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

package org.ylzl.eden.dynamic.cache.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.ylzl.eden.dynamic.cache.l1cache.L1CacheLoader;
import org.ylzl.eden.dynamic.cache.l1cache.L1CacheRemovalListener;
import org.ylzl.eden.dynamic.cache.spring.boot.env.CacheProperties;
import org.ylzl.eden.dynamic.cache.support.spring.DynamicCacheManager;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import java.util.List;

/**
 * 动态缓存自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = CacheProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@EnableCaching(proxyTargetClass = true)
@EnableConfigurationProperties(CacheProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class DynamicCacheAutoConfiguration {

	private final CacheProperties cacheProperties;

	private final ObjectProvider<L1CacheRemovalListener> l1CacheRemovalListeners;

	private final ObjectProvider<L1CacheLoader> l1CacheLoaders;

	private final ObjectProvider<RedissonClient> redissonClients;

	@Bean
	public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<List<CacheManagerCustomizer<?>>> customizers) {
		return new CacheManagerCustomizers(customizers.getIfAvailable());
	}

	@Primary
	@Bean
	public DynamicCacheManager dynamicCacheManager(CacheManagerCustomizers customizers) {
		DynamicCacheManager cacheManager = new DynamicCacheManager(cacheProperties);
		if (l1CacheRemovalListeners.getIfAvailable() != null) {
		   cacheManager.setL1CacheRemovalListener(l1CacheRemovalListeners.getIfAvailable());
		}
		if (l1CacheLoaders.getIfAvailable() != null) {
			cacheManager.setL1CacheLoader(l1CacheLoaders.getIfAvailable());
		}
		if (redissonClients.getIfAvailable() != null) {
			cacheManager.setL2CacheClient(redissonClients.getIfAvailable());
		}
		return customizers.customize(cacheManager);
	}
}
