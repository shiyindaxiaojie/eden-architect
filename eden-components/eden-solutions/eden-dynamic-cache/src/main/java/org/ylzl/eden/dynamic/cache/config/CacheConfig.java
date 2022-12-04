package org.ylzl.eden.dynamic.cache.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.ylzl.eden.dynamic.cache.enums.CacheType;
import org.ylzl.eden.commons.id.NanoIdUtils;

import java.util.HashMap;
import java.util.Map;

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

	private final Caffeine caffeine = new Caffeine();

	@EqualsAndHashCode
	@ToString
	@Accessors(chain = true)
	@Getter
	@Setter
	public static class Caffeine {

		/**
		 * 是否自动刷新过期缓存
		 */
		private boolean autoRefreshExpireCache = true;

		/**
		 * 自动刷新线程池大小
		 */
		private Integer autoRefreshPoolSize = Runtime.getRuntime().availableProcessors();

		/**
		 * 自动刷新间隔（秒）
		 */
		private Integer autoRefreshInSeconds = 30;

		/**
		 * 默认配置
		 */
		private String defaultSpec;

		/**
		 * 附加配置
		 */
		private Map<String, String> specs = new HashMap<>();
	}
}
