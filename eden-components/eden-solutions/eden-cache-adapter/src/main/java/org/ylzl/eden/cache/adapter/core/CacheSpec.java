package org.ylzl.eden.cache.adapter.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 缓存配置共享
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@Getter
@Setter
public class CacheSpec {

	/**
	 * 缓存过期时间（毫秒）
	 */
	private long expireInMs;

	/**
	 * 最大容量
	 */
	private int maxSize;
}
