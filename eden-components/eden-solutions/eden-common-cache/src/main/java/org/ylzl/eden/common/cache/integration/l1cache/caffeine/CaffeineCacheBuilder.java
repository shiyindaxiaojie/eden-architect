package org.ylzl.eden.common.cache.integration.l1cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.common.cache.builder.AbstractCacheBuilder;
import org.ylzl.eden.common.cache.config.CacheConfig;
import org.ylzl.eden.common.cache.config.CacheSpec;
import org.ylzl.eden.common.cache.expire.CacheExpiredCause;
import org.ylzl.eden.common.cache.expire.CacheExpiredListener;
import org.ylzl.eden.common.cache.loader.CacheLoader;
import org.ylzl.eden.commons.lang.StringUtils;
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
			throw new RuntimeException("Please setting caffeine spec config");
		}
		CustomCaffeineSpec newCaffeineSpec = CustomCaffeineSpec.parse(spec);
		caffeineSpecMap.put(cacheName, newCaffeineSpec);
		return newCaffeineSpec;
	}

	private String getCaffeineSpec(String cacheName, CacheConfig cacheConfig) {
		if (StringUtils.isBlank(cacheName)) {
			return cacheConfig.getCaffeine().getDefaultSpec();
		}
		String spec = cacheConfig.getCaffeine().getSpecs().get(cacheName);
		if (StringUtils.isBlank(spec)) {
			return cacheConfig.getCaffeine().getDefaultSpec();
		}
		return spec;
	}
}
