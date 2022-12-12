/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.dynamic.cache.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ylzl.eden.commons.id.NanoIdUtils;
import org.ylzl.eden.dynamic.cache.enums.CacheType;

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
@Setter
@Getter
public class CacheConfig {

	/** 缓存实例ID，默认使用 NanoId */
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
	 * 是否存储 NullValue，可防止缓存穿透
	 */
	private boolean allowNullValues = true;

	/**
	 * NullValue 的过期时间（秒）
	 */
	private int nullValueExpireInSeconds = 60;

	/**
	 * NullValue 的最大数量，超出该值后的下一次刷新缓存将进行淘汰
	 */
	private int nullValueMaxSize = 2048;

	/**
	 * NullValue 的清理频率
	 */
	private int nullValueClearPeriodSeconds = 10;

	private final Composite composite = new Composite();

	private final Caffeine caffeine = new Caffeine();

	private final Guava guava = new Guava();

	private final Ehcache ehcache = new Ehcache();

	private final Redis redis = new Redis();

	private final Dragonfly dragonfly = new Dragonfly();

	private final Memcached memcached = new Memcached();

	private final Hazelcast hazelcast = new Hazelcast();

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Composite {

		/** 一级缓存类型 */
		private String l1CacheType = CacheType.CAFFEINE.name();

		/** 二级缓存类型*/
		private String l2CacheType = CacheType.REDIS.name();

		/** 是否开启一级缓存，默认开启 */
		private boolean l1CacheEnabled = true;
	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Caffeine {

		/**
		 * 是否自动刷新过期缓存
		 */
		private boolean autoRefreshExpireCache = true;

		/**
		 * 自动刷新缓存的线程池大小
		 */
		private int autoRefreshPoolSize = Runtime.getRuntime().availableProcessors();

		/**
		 * 自动刷新缓存的时间间隔（秒）
		 */
		private int autoRefreshInSeconds = 30;

		/**
		 * 默认配置
		 */
		private String defaultSpec;

		/**
		 * 指定配置
		 */
		private Map<String, String> specs = new HashMap<>();
	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Guava {

		/**
		 * 是否自动刷新过期缓存
		 */
		private boolean autoRefreshExpireCache = true;

		/**
		 * 自动刷新缓存的线程池大小
		 */
		private int autoRefreshPoolSize = Runtime.getRuntime().availableProcessors();

		/**
		 * 自动刷新缓存的时间间隔（秒）
		 */
		private int autoRefreshInSeconds = 30;

		/**
		 * 默认配置
		 */
		private String defaultSpec;

		/**
		 * 指定配置
		 */
		private Map<String, String> specs = new HashMap<>();
	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Ehcache {

	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Redis {

	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Dragonfly {

	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Memcached {

	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Hazelcast {

	}
}
