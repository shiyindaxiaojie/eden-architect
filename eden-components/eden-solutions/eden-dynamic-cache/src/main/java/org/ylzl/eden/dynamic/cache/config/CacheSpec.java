package org.ylzl.eden.dynamic.cache.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 缓存共享配置
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
	private int maximumSize;
}
