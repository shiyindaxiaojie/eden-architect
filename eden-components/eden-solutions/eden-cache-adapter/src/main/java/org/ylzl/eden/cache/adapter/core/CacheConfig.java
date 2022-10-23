package org.ylzl.eden.cache.adapter.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 缓存配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@Getter
@Setter
public class CacheConfig {

	/**
	 * 允许动态创建缓存
	 */
	private boolean allowDynamicCreate = true;

	/**
	 * 缓存类型
	 */
	private CacheType cacheType = CacheType.COMPOSITE;
}
