package org.ylzl.eden.dynamic.cache.loader;

import org.ylzl.eden.dynamic.cache.config.CacheSpec;
import org.ylzl.eden.dynamic.cache.level.L2Cache;
import org.ylzl.eden.dynamic.cache.sync.CacheSynchronizer;

import java.util.concurrent.Callable;

/**
 * 本地缓存加载器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class LocalCacheLoader implements CacheLoader<Object, Object> {

	/**
	 * 根据 Key 加载 Value
	 *
	 * @param key 缓存Key
	 * @return Value
	 */
	@Override
	public Object load(Object key) {
		return null;
	}

	/**
	 * 添加 ValueLoader
	 *
	 * @param key         缓存Key
	 * @param valueLoader ValueLoader 实例
	 */
	@Override
	public void addValueLoader(Object key, Callable<?> valueLoader) {

	}

	/**
	 * 移除 ValueLoader
	 *
	 * @param key 缓存Key
	 */
	@Override
	public void removeValueLoader(Object key) {

	}

	/**
	 * 设置二级缓存
	 *
	 * @param l2Cache 二级缓存实例
	 */
	@Override
	public void setL2Cache(L2Cache l2Cache) {

	}

	/**
	 * 设置缓存共享配置
	 *
	 * @param cacheSpec 缓存共享配置
	 */
	@Override
	public void setCacheSpec(CacheSpec cacheSpec) {

	}

	/**
	 * 设置缓存同步器
	 *
	 * @param cacheSynchronizer 缓存同步器
	 */
	@Override
	public void setCacheSynchronizer(CacheSynchronizer cacheSynchronizer) {

	}

	/**
	 * 设置是否允许空值
	 *
	 * @param allowNullValues 是否允许空值
	 */
	@Override
	public void setAllowNullValues(boolean allowNullValues) {

	}
}
