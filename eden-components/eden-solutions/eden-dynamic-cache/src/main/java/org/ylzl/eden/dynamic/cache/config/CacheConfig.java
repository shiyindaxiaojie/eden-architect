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
import org.redisson.config.Config;
import org.ylzl.eden.dynamic.cache.CacheType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

	/** 缓存开关 */
	private boolean enabled = true;

	/** 缓存类型 */
	private String cacheType = CacheType.COMPOSITE.name();

	/** 是否存储 NullValue，可防止缓存穿透 */
	private boolean allowNullValues = true;

	/** NullValue 的最大数量 */
	private int nullValueMaximumSize = 2048;

	/** NullValue 的过期时间（秒）*/
	private int nullValueTimeToLive = 60;

	/** NullValue 的清理频率（秒） */
	private int nullValueRetentionInterval = 10;

	/** Key 分隔符 */
	private String keySeparator = ":";

	private final Composite composite = new Composite();

	private final L1Cache l1Cache = new L1Cache();

	private final L2Cache l2Cache = new L2Cache();

	private final HotKey hotKey = new HotKey();

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class Composite {

		/** 一级缓存类型 */
		private String l1CacheType = CacheType.CAFFEINE.name();

		/** 二级缓存类型 */
		private String l2CacheType = CacheType.REDIS.name();

		/** 热Key类型 */
		private String hotKeyType;
	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class L1Cache {

		/** 是否开启一级缓存，默认开启 */
		private boolean enabled = true;

		/** 缓存Key集合，默认不设置表示全部生效 */
		private Set<String> cacheKeys = new HashSet<>();

		/** CacheName集合，默认不设置表示全部生效 */
		private Set<String> cacheNames = new HashSet<>();

		/** 初始容量 */
		private int initialCapacity;

		/** 最大容量 */
		private long maximumSize;

		private final Caffeine caffeine = new Caffeine();

		private final Guava guava = new Guava();

		@EqualsAndHashCode
		@ToString
		@Setter
		@Getter
		public static class Caffeine {

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
			 * 默认配置
			 */
			private String defaultSpec;

			/**
			 * 指定配置
			 */
			private Map<String, String> specs = new HashMap<>();
		}
	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class L2Cache {

		private final Redis redis = new Redis();

		private final Dragonfly dragonfly = new Dragonfly();

		private final Hazelcast hazelcast = new Hazelcast();

		@EqualsAndHashCode
		@ToString
		@Setter
		@Getter
		public static class Redis {

			/** 尝试加锁 */
			private boolean tryLock = true;

			/** 默认过期时间（秒） */
			private int timeToLive = 60;

			/** Redisson 配置 */
			private Config config;
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
		public static class Hazelcast {

		}
	}

	@EqualsAndHashCode
	@ToString
	@Setter
	@Getter
	public static class HotKey {

		private final JD jd = new JD();

		private final Sentinel sentinel = new Sentinel();

		@EqualsAndHashCode
		@ToString
		@Setter
		@Getter
		public static class JD {

			private boolean enabled;

			private String appName;

			private String etcdServer;
		}

		@EqualsAndHashCode
		@ToString
		@Setter
		@Getter
		public static class Sentinel {

			private boolean enabled;

			private Transport transport = new Transport();

			private DataSource datasource = new DataSource();

			@EqualsAndHashCode
			@ToString
			@Setter
			@Getter
			public static class Transport {

				private String port = "8719";

				private String dashboard = "localhost:8719";

				private String heartbeatIntervalMs;

				private String clientIp;
			}

			@EqualsAndHashCode
			@ToString
			@Setter
			@Getter
			public static class DataSource {

				private Nacos nacos = new Nacos();

				private Apollo apollo = new Apollo();

				private Zookeeper zk = new Zookeeper();

				private Etcd etcd = new Etcd();

				@EqualsAndHashCode
				@ToString
				@Setter
				@Getter
				public static class Nacos {

					private String serverAddr;

					private String username;

					private String password;

					private String groupId = "DEFAULT_GROUP";

					private String dataId;

					private String endpoint;

					private String namespace;

					private String accessKey;

					private String secretKey;
				}

				@EqualsAndHashCode
				@ToString
				@Setter
				@Getter
				public static class Apollo {

					private String serverAddr = "http://localhost:8080";

					private String namespaceName;

					private String flowRulesKey;

					private String defaultFlowRuleValue;
				}

				@EqualsAndHashCode
				@ToString
				@Setter
				@Getter
				public static class Zookeeper {

					private String serverAddr = "localhost:2181";

					private String path;

					private String groupId;

					private String dataId;
				}

				@EqualsAndHashCode
				@ToString
				@Setter
				@Getter
				public static class Etcd {

					private String endPoints;

					private String user;

					private String password;

					private String charset = "UTF-8";

					private boolean auth = true;

					private String ruleKey;
				}
			}
		}
	}
}
