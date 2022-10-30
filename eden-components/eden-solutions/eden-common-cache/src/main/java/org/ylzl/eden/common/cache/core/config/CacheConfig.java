package org.ylzl.eden.common.cache.core.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.ylzl.eden.common.cache.core.CacheType;
import org.ylzl.eden.commons.id.NanoIdUtils;

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
	 * 缓存实例ID，默认使用 NanoId
	 */
	private String instanceId = NanoIdUtils.randomNanoId();

	/**
	 * 缓存类型
	 *
	 * @see CacheType
	 */
	private String cacheType = CacheType.COMPOSITE.name();

	/**
	 * 允许动态创建缓存
	 */
	private boolean dynamic = true;

	/**
	 * 是否存储NULL，可防止缓存穿透
	 */
	private boolean allowNullValues = true;

	/**
	 * NULL值的过期时间（秒）
	 */
	private int nullValueExpireInSeconds = 60;

	/**
	 * NULL值的最大数量，超出该值后的下一次刷新缓存将进行淘汰
	 */
	private int nullValueMaxSize = 2048;
}
