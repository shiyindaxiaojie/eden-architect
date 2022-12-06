package org.ylzl.eden.dynamic.cache.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.ylzl.eden.dynamic.cache.Cache;

import java.util.concurrent.Callable;

/**
 * Spring 缓存
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class SpringCache extends AbstractValueAdaptingCache {

	/**
	 * 缓存名称
	 */
	private final String name;

	/**
	 * 缓存接口
	 */
	private final Cache cache;

	/**
	 * 有参构造函数
	 *
	 * @param allowNullValues
	 * @param name
	 * @param cache
	 */
	public SpringCache(boolean allowNullValues, String name, Cache cache) {
		super(allowNullValues);
		this.name = name;
		this.cache = cache;
	}

	@Override
	public @NotNull String getName() {
		return this.name;
	}

	@Override
	public @NotNull Object getNativeCache() {
		return this.cache;
	}

	@Override
	protected Object lookup(@NotNull Object key) {
		return this.cache.get(key);
	}

	@Override
	public ValueWrapper get(@NotNull Object key) {
		return toValueWrapper(this.cache.get(key));
	}

	@Override
	public <T> T get(@NotNull Object key, @NotNull Callable<T> valueLoader) {
		return this.cache.get(key, valueLoader);
	}

	@Override
	public void put(@NotNull Object key, Object value) {
		this.cache.put(key, value);
	}

	@Override
	public ValueWrapper putIfAbsent(@NotNull Object key, Object value) {
		return toValueWrapper(this.cache.putIfAbsent(key, value));
	}

	@Override
	public void evict(@NotNull Object key) {
		this.cache.evict(key);
	}

	@Override
	public void clear() {
		this.cache.clear();
	}
}
