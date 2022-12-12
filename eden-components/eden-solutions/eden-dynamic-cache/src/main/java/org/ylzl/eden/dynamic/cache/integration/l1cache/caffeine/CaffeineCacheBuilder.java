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

package org.ylzl.eden.dynamic.cache.integration.l1cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.dynamic.cache.builder.AbstractCacheBuilder;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.config.CacheSpec;
import org.ylzl.eden.dynamic.cache.exception.CacheSpecException;
import org.ylzl.eden.dynamic.cache.expire.CacheExpiredCause;
import org.ylzl.eden.dynamic.cache.expire.CacheExpiredListener;
import org.ylzl.eden.dynamic.cache.loader.CacheLoader;
import org.ylzl.eden.extension.ExtensionLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Caffeine 缓存构建器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
public class CaffeineCacheBuilder extends AbstractCacheBuilder<CaffeineCache> {

	private static final Map<String, CustomCaffeineSpec> caffeineSpecMap = new HashMap<>(16);

	@Override
	public CaffeineCache build(String cacheName) {
		CacheLoader<Object, Object> cacheLoader = ExtensionLoader.getExtensionLoader(CacheLoader.class).getDefaultExtension();
		cacheLoader.setCacheSpec(this.parseSpec(cacheName));
		cacheLoader.setCacheSynchronizer(this.getCacheSynchronizer());
		cacheLoader.setAllowNullValues(this.getCacheConfig().isAllowNullValues());

		Cache<Object, Object> cache = this.buildCacheClient(cacheName, this.getCacheConfig(),
			cacheLoader, this.getExpiredListener());

		return new CaffeineCache(cacheName, this.getCacheConfig(), cache);
	}

	@Override
	public CacheSpec parseSpec(String cacheName) {
		CustomCaffeineSpec caffeineSpec = this.buildCaffeineSpec(cacheName, this.getCacheConfig());
		CacheSpec cacheSpec = new CacheSpec();
		cacheSpec.setExpireInMs(caffeineSpec.getExpireInMs());
		cacheSpec.setMaximumSize((int) caffeineSpec.getMaximumSize());
		return cacheSpec;
	}

	private Cache<Object, Object> buildCacheClient(String cacheName, CacheConfig cacheConfig,
												   CacheLoader<Object, Object> cacheLoader,
												   CacheExpiredListener<Object, Object> cacheExpiredListener) {
		CustomCaffeineSpec caffeineSpec = this.buildCaffeineSpec(cacheName, cacheConfig);
		Caffeine<Object, Object> cacheBuilder = caffeineSpec.toBuilder();

		if (cacheExpiredListener != null) {
			cacheBuilder.removalListener((key, value, cause) -> {
				cacheExpiredListener.onExpired(key, value, CacheExpiredCause.parse(cause.name()));
			});
		}

		if (cacheLoader == null) {
			log.info("Create a native caffeine cache instance, cacheName={}", cacheName);
			return cacheBuilder.build();
		}

		log.info("Create a native caffeine loadingCache instance, cacheName={}", cacheName);
		return cacheBuilder.build(cacheLoader::load);
	}

	private CustomCaffeineSpec buildCaffeineSpec(String cacheName, CacheConfig cacheConfig) {
		CustomCaffeineSpec caffeineSpec = caffeineSpecMap.get(cacheName);
		if (caffeineSpec != null) {
			return caffeineSpec;
		}

		String spec = this.getCaffeineSpec(cacheName, cacheConfig);
		if (StringUtils.isBlank(spec)) {
			throw new CacheSpecException("Please setting caffeine spec config");
		}

		CustomCaffeineSpec parseCaffeineSpec = CustomCaffeineSpec.parse(spec);
		caffeineSpecMap.put(cacheName, parseCaffeineSpec);
		return parseCaffeineSpec;
	}

	private String getCaffeineSpec(String cacheName, CacheConfig cacheConfig) {
		String spec = cacheConfig.getCaffeine().getSpecs().get(cacheName);
		if (StringUtils.isBlank(spec)) {
			return cacheConfig.getCaffeine().getDefaultSpec();
		}
		return spec;
	}
}
