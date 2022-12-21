package org.ylzl.eden.dynamic.cache.composite;

import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.commons.lang.MessageFormatUtils;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.dynamic.cache.Cache;
import org.ylzl.eden.dynamic.cache.CacheType;
import org.ylzl.eden.dynamic.cache.L1Cache;
import org.ylzl.eden.dynamic.cache.L2Cache;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.hotkey.HotKey;
import org.ylzl.eden.dynamic.cache.support.AbstractAdaptingCache;
import org.ylzl.eden.extension.ExtensionLoader;

import java.util.Set;
import java.util.concurrent.Callable;

/**
 * 多级缓存
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class CompositeCache extends AbstractAdaptingCache implements Cache {

	private final CacheConfig cacheConfig;

	private final L1Cache l1Cache;

	private final L2Cache l2Cache;

	public CompositeCache(String cacheName, CacheConfig cacheConfig, L1Cache l1Cache, L2Cache l2Cache) {
		super(cacheName, cacheConfig);
		this.cacheConfig = cacheConfig;
		this.l1Cache = l1Cache;
		this.l2Cache = l2Cache;
	}

	/**
	 * 获取缓存类型
	 *
	 * @return 缓存类型
	 */
	@Override
	public String getCacheType() {
		return CacheType.COMPOSITE.name();
	}

	/**
	 * 使用原生缓存实例
	 *
	 * @return 原生缓存实例
	 */
	@Override
	public CompositeCache getNativeCache() {
		return this;
	}

	/**
	 * 获取指定key的缓存项
	 *
	 * @param key 缓存Key
	 * @return 缓存项
	 */
	@Override
	public Object get(Object key) {
		boolean isL1CacheEnabled = isL1CacheEnabled(key);
		if (isL1CacheEnabled) {
			Object l1CacheValue = l1Cache.get(key);
			if (l1Cache.isLoadingCache() || l1CacheValue != null) {
				return l1CacheValue;
			}
		}
		Object l2CacheValue = l2Cache.get(key);
		if (l2CacheValue != null && isL1CacheEnabled) {
			l1Cache.put(key, l2CacheValue);
		}
		return l2CacheValue;
	}

	/**
	 * 获取指定key的缓存项
	 *
	 * @param key  缓存Key
	 * @param type 缓存类型
	 * @return 缓存项
	 */
	@Override
	public <T> T get(Object key, Class<T> type) {
		Object value = get(key);
		if (value == null) {
			return null;
		}
		if (type != null && !type.isInstance(value)) {
			throw new IllegalStateException(MessageFormatUtils.format(
				"Composite Cached value '{}' is not of required type '{}'", value, type.getName()));
		}
		return (T) value;
	}

	/**
	 * 获取指定key的缓存项，如果缓存项不存在，则通过 {@code valueLoader} 获取值
	 *
	 * @param key         缓存Key
	 * @param valueLoader 缓存加载器
	 * @return 缓存项
	 */
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		if (isL1CacheEnabled(key)) {
			return l1Cache.get(key, valueLoader);
		}
		return l2Cache.get(key, valueLoader);
	}

	/**
	 * 设置指定key的缓存项
	 *
	 * @param key   缓存Key
	 * @param value 缓存项
	 */
	@Override
	public void put(Object key, Object value) {
		l2Cache.put(key, value);
		if (isL1CacheEnabled(key)) {
			l1Cache.put(key, value);
		}
	}

	/**
	 * 删除指定的缓存项
	 *
	 * @param key 缓存Key
	 */
	@Override
	public void evict(Object key) {
		l2Cache.evict(key);
		if (isL1CacheEnabled(key)) {
			l1Cache.evict(key);
		}
	}

	/**
	 * 清空缓存
	 */
	@Override
	public void clear() {
		l2Cache.clear();
		l1Cache.clear();
	}

	/**
	 * 判断Key是否存在
	 *
	 * @param key Key
	 * @return 是否存在
	 */
	@Override
	public boolean isExists(Object key) {
		if (isL1CacheEnabled(key)) {
			return l1Cache.isExists(key);
		}
		return l2Cache.isExists(key);
	}

	/**
	 * 是否开启一级缓存
	 *
	 * @param key Key
	 * @return 是否开启
	 */
	private boolean isL1CacheEnabled(Object key) {
		if (!cacheConfig.getL1Cache().isEnabled()) {
			return false;
		}

		Set<String> cacheNames = cacheConfig.getL1Cache().getCacheNames();
		if (CollectionUtils.isNotEmpty(cacheNames) && cacheNames.contains(this.getName())) {
			return true;
		}

		Set<String> cacheKeys = cacheConfig.getL1Cache().getCacheKeys();
		if (CollectionUtils.isNotEmpty(cacheKeys) && cacheKeys.contains(key.toString())) {
			return true;
		}

		String hotKeyType = this.cacheConfig.getComposite().getHotKeyType();
		if (StringUtils.isNotBlank(hotKeyType)) {
			HotKey hotKey = ExtensionLoader.getExtensionLoader(HotKey.class).getExtension(hotKeyType);
			return hotKey.isHotKey(key);
		}
		return false;
	}
}
