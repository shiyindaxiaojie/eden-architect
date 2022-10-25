package org.ylzl.eden.cache.adapter.core.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.ylzl.eden.cache.adapter.core.CacheType;
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
	 */
	private CacheType cacheType = CacheType.COMPOSITE;

	/**
	 * 允许动态创建缓存
	 */
	private boolean dynamic = true;

	/**
	 * 空值处理
	 */
	private final NullValue nullValue = new NullValue();

	@Accessors(chain = true)
	@Getter
	@Setter
	public static class NullValue {

		/**
		 * 是否存储NULL，可防止缓存穿透
		 */
		private boolean enabled = true;

		/**
		 * NULL值的过期时间（秒）
		 */
		private int expireInSeconds = 60;

		/**
		 * NULL值的最大数量，超出该值后的下一次刷新缓存将进行淘汰
		 */
		private int maxSize = 3000;
	}

	@Accessors(chain = true)
	@Getter
	@Setter
	public static class Sync {

		/**
		 *
		 */
		private String type;


	}
}
