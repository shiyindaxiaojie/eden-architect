package org.ylzl.eden.cache.adapter.core.spring;

import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.ylzl.eden.cache.adapter.core.Cache;

import java.util.concurrent.Callable;

/**
 * TODO
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
	 * 有参构造器
	 *
	 * @param name
	 * @param cache
	 * @param allowNullValues
	 */
	public SpringCache(String name, Cache cache, boolean allowNullValues) {
		super(allowNullValues);
		this.name = name;
		this.cache = cache;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return this.cache;
	}

	@Override
	protected Object lookup(Object key) {
		return this.cache.get(key);
	}

	@Override
	public ValueWrapper get(Object key) {
		return toValueWrapper(this.cache.get(key));
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		return this.cache.get(key, valueLoader);
	}

	@Override
	public void put(Object key, Object value) {
		this.cache.put(key, value);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		return toValueWrapper(this.cache.putIfAbsent(key, value));
	}

	@Override
	public void evict(Object key) {
		this.cache.evict(key);
	}

	@Override
	public void clear() {
		this.cache.clear();
	}
}
