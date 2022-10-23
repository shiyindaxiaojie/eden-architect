package org.ylzl.eden.cache.adapter.core.builder;

import org.ylzl.eden.cache.adapter.core.Cache;
import org.ylzl.eden.cache.adapter.core.CacheSpec;

import java.io.Serializable;

/**
 * 缓存构造器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface CacheBuilder<T extends Cache> extends Serializable {

	/**
	 * 构建指定名称的缓存对象
	 *
	 * @param cacheName
	 * @return
	 */
	T build(String cacheName);

	/**
	 * 解析指定名称的缓存配置
	 *
	 * @param cacheName
	 * @return
	 */
	CacheSpec parseSpec(String cacheName);
}
