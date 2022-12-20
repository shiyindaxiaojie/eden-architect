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

import lombok.*;
import org.ylzl.eden.dynamic.cache.CacheType;

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

	/** 缓存类型 */
	private String cacheType = CacheType.COMPOSITE.name();

	/** 是否存储 NullValue，可防止缓存穿透 */
	private boolean allowNullValues = true;

	/** NullValue 的最大数量 */
	private int nullValueMaximumSize = 2048;

	/** NullValue 的过期时间（秒）*/
	private int nullValueExpireInSeconds = 60;

	/** NullValue 的清理频率（秒） */
	private int nullValueRetentionInterval = 10;

	private final Composite composite = new Composite();

	private final L1Cache l1Cache = new L1Cache();

	private final L2Cache l2Cache = new L2Cache();

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
	public static class L1Cache {

		/** 初始容量 */
		private int initialCapacity;

		/** 最大容量 */
		private long maximumSize;

		private final Caffeine caffeine = new Caffeine();

		private final Guava guava = new Guava();

		private final Ehcache ehcache = new Ehcache();

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
	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class L2Cache {

		private final Redis redis = new Redis();

		private final Dragonfly dragonfly = new Dragonfly();

		private final Memcached memcached = new Memcached();

		private final Hazelcast hazelcast = new Hazelcast();

		@EqualsAndHashCode
		@ToString
		@Setter
		@Getter
		public static class Redis {

			/** Key 分隔符 */
			private String spilt = ":";

			/** 尝试加锁 */
			private boolean tryLock = true;

			/** 默认过期时间（秒） */
			private int defaultExpireInSeconds = 60;
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
}
