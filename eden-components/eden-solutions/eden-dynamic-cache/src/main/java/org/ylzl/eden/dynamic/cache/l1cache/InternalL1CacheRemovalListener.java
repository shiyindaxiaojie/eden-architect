package org.ylzl.eden.dynamic.cache.l1cache;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.ylzl.eden.dynamic.cache.Cache;
import org.ylzl.eden.dynamic.cache.L2Cache;
import org.ylzl.eden.dynamic.cache.composite.CompositeCache;
import org.ylzl.eden.dynamic.cache.support.value.NullValue;

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
