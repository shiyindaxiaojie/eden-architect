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

package org.ylzl.eden.dynamic.mq.integration.kafka.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.core.io.Resource;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer;
import org.springframework.util.unit.DataSize;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Kafka 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class KafkaConfig {

	/**
	 * Comma-delimited list of host:port pairs to use for establishing the initial
	 * connections to the Kafka cluster. Applies to all components unless overridden.
	 */
	private List<String> bootstrapServers = new ArrayList<>(Collections.singletonList("localhost:9092"));

	/**
	 * ID to pass to the server when making requests. Used for server-side logging.
	 */
	private String clientId;

	/**
	 * Additional properties, common to producers and consumers, used to configure the
	 * client.
	 */
	private final Map<String, String> properties = new HashMap<>();

	private final Consumer consumer = new Consumer();

	private final Producer producer = new Producer();

	private final Admin admin = new Admin();

	private final Streams streams = new Streams();

	private final Listener listener = new Listener();

	private final Ssl ssl = new Ssl();

	private final Jaas jaas = new Jaas();

	private final Template template = new Template();

	private final Security security = new Security();

	@Getter
	@Setter
	public static class Consumer {

		private final Ssl ssl = new Ssl();

		private final Security security = new Security();

		/**
		 * Frequency with which the consumer offsets are auto-committed to Kafka if
		 * 'enable.auto.commit' is set to true.
		 */
		private Duration autoCommitInterval;

		/**
		 * What to do when there is no initial offset in Kafka or if the current offset no
		 * longer exists on the server.
		 */
		private String autoOffsetReset;

		/**
		 * Comma-delimited list of host:port pairs to use for establishing the initial
		 * connections to the Kafka cluster. Overrides the global property, for consumers.
		 */
		private List<String> bootstrapServers;

		/**
		 * ID to pass to the server when making requests. Used for server-side logging.
		 */
		private String clientId;

		/**
		 * Whether the consumer's offset is periodically committed in the background.
		 */
		private Boolean enableAutoCommit;

		/**
		 * Maximum amount of time the server blocks before answering the fetch request if
		 * there isn't sufficient data to immediately satisfy the requirement given by
		 * "fetch-min-size".
		 */
		private Duration fetchMaxWait;

		/**
		 * Minimum amount of data the server should return for a fetch request.
		 */
		private DataSize fetchMinSize;

		/**
		 * Unique string that identifies the consumer group to which this consumer
		 * belongs.
		 */
		private String groupId;

		/**
		 * Expected time between heartbeats to the consumer coordinator.
		 */
		private Duration heartbeatInterval;

		/**
		 * Isolation level for reading messages that have been written transactionally.
		 */
		private IsolationLevel isolationLevel = IsolationLevel.READ_UNCOMMITTED;

		/**
		 * Deserializer class for keys.
		 */
		private Class<?> keyDeserializer = StringDeserializer.class;

		/**
		 * Deserializer class for values.
		 */
		private Class<?> valueDeserializer = StringDeserializer.class;

		/**
		 * Maximum number of records returned in a single call to poll().
		 */
		private Integer maxPollRecords;

		/**
		 * Additional consumer-specific properties used to configure the client.
		 */
		private final Map<String, String> properties = new HashMap<>();

		public Map<String, Object> buildProperties() {
			Properties properties = new Properties();
			PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
			map.from(this::getAutoCommitInterval).asInt(Duration::toMillis)
				.to(properties.in(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG));
			map.from(this::getAutoOffsetReset).to(properties.in(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));
			map.from(this::getBootstrapServers).to(properties.in(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
			map.from(this::getClientId).to(properties.in(ConsumerConfig.CLIENT_ID_CONFIG));
			map.from(this::getEnableAutoCommit).to(properties.in(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG));
			map.from(this::getFetchMaxWait).asInt(Duration::toMillis)
				.to(properties.in(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG));
			map.from(this::getFetchMinSize).asInt(DataSize::toBytes)
				.to(properties.in(ConsumerConfig.FETCH_MIN_BYTES_CONFIG));
			map.from(this::getGroupId).to(properties.in(ConsumerConfig.GROUP_ID_CONFIG));
			map.from(this::getHeartbeatInterval).asInt(Duration::toMillis)
				.to(properties.in(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG));
			map.from(() -> getIsolationLevel().name().toLowerCase(Locale.ROOT))
				.to(properties.in(ConsumerConfig.ISOLATION_LEVEL_CONFIG));
			map.from(this::getKeyDeserializer).to(properties.in(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
			map.from(this::getValueDeserializer).to(properties.in(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
			map.from(this::getMaxPollRecords).to(properties.in(ConsumerConfig.MAX_POLL_RECORDS_CONFIG));
			return properties.with(this.ssl, this.security, this.properties);
		}
	}

	@Getter
	@Setter
	public static class Producer {

		private final Ssl ssl = new Ssl();

		private final Security security = new Security();

		/**
		 * Number of acknowledgments the producer requires the leader to have received
		 * before considering a request complete.
		 */
		private String acks;

		/**
		 * Default batch size. A small batch size will make batching less common and may
		 * reduce throughput (a batch size of zero disables batching entirely).
		 */
		private DataSize batchSize;

		/**
		 * Comma-delimited list of host:port pairs to use for establishing the initial
		 * connections to the Kafka cluster. Overrides the global property, for producers.
		 */
		private List<String> bootstrapServers;

		/**
		 * Total memory size the producer can use to buffer records waiting to be sent to
		 * the server.
		 */
		private DataSize bufferMemory;

		/**
		 * ID to pass to the server when making requests. Used for server-side logging.
		 */
		private String clientId;

		/**
		 * Compression type for all data generated by the producer.
		 */
		private String compressionType;

		/**
		 * Serializer class for keys.
		 */
		private Class<?> keySerializer = StringSerializer.class;

		/**
		 * Serializer class for values.
		 */
		private Class<?> valueSerializer = StringSerializer.class;

		/**
		 * When greater than zero, enables retrying of failed sends.
		 */
		private Integer retries;

		/**
		 * When non empty, enables transaction support for producer.
		 */
		private String transactionIdPrefix;

		/**
		 * Additional producer-specific properties used to configure the client.
		 */
		private final Map<String, String> properties = new HashMap<>();

		public Map<String, Object> buildProperties() {
			Properties properties = new Properties();
			PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
			map.from(this::getAcks).to(properties.in(ProducerConfig.ACKS_CONFIG));
			map.from(this::getBatchSize).asInt(DataSize::toBytes).to(properties.in(ProducerConfig.BATCH_SIZE_CONFIG));
			map.from(this::getBootstrapServers).to(properties.in(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
			map.from(this::getBufferMemory).as(DataSize::toBytes)
				.to(properties.in(ProducerConfig.BUFFER_MEMORY_CONFIG));
			map.from(this::getClientId).to(properties.in(ProducerConfig.CLIENT_ID_CONFIG));
			map.from(this::getCompressionType).to(properties.in(ProducerConfig.COMPRESSION_TYPE_CONFIG));
			map.from(this::getKeySerializer).to(properties.in(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
			map.from(this::getRetries).to(properties.in(ProducerConfig.RETRIES_CONFIG));
			map.from(this::getValueSerializer).to(properties.in(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
			return properties.with(this.ssl, this.security, this.properties);
		}
	}

	@Getter
	@Setter
	public static class Admin {

		private final Ssl ssl = new Ssl();

		private final Security security = new Security();

		/**
		 * ID to pass to the server when making requests. Used for server-side logging.
		 */
		private String clientId;

		/**
		 * Additional admin-specific properties used to configure the client.
		 */
		private final Map<String, String> properties = new HashMap<>();

		/**
		 * Whether to fail fast if the broker is not available on startup.
		 */
		private boolean failFast;

		public Map<String, Object> buildProperties() {
			Properties properties = new Properties();
			PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
			map.from(this::getClientId).to(properties.in(ProducerConfig.CLIENT_ID_CONFIG));
			return properties.with(this.ssl, this.security, this.properties);
		}

	}

	/**
	 * High (and some medium) priority Streams properties and a general properties bucket.
	 */
	@Getter
	@Setter
	public static class Streams {

		private final Ssl ssl = new Ssl();

		private final Security security = new Security();

		private final Cleanup cleanup = new Cleanup();

		/**
		 * Kafka streams application.id property; default spring.application.name.
		 */
		private String applicationId;

		/**
		 * Whether or not to auto-start the streams factory bean.
		 */
		private boolean autoStartup = true;

		/**
		 * Comma-delimited list of host:port pairs to use for establishing the initial
		 * connections to the Kafka cluster. Overrides the global property, for streams.
		 */
		private List<String> bootstrapServers;

		/**
		 * Maximum memory size to be used for buffering across all threads.
		 */
		private DataSize cacheMaxSizeBuffering;

		/**
		 * ID to pass to the server when making requests. Used for server-side logging.
		 */
		private String clientId;

		/**
		 * The replication factor for change log topics and repartition topics created by
		 * the stream processing application.
		 */
		private Integer replicationFactor;

		/**
		 * Directory location for the state store.
		 */
		private String stateDir;

		/**
		 * Additional Kafka properties used to configure the streams.
		 */
		private final Map<String, String> properties = new HashMap<>();

		public Map<String, Object> buildProperties() {
			Properties properties = new Properties();
			PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
			map.from(this::getApplicationId).to(properties.in("application.id"));
			map.from(this::getBootstrapServers).to(properties.in(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG));
			map.from(this::getCacheMaxSizeBuffering).asInt(DataSize::toBytes)
				.to(properties.in("cache.max.bytes.buffering"));
			map.from(this::getClientId).to(properties.in(CommonClientConfigs.CLIENT_ID_CONFIG));
			map.from(this::getReplicationFactor).to(properties.in("replication.factor"));
			map.from(this::getStateDir).to(properties.in("state.dir"));
			return properties.with(this.ssl, this.security, this.properties);
		}
	}

	@Getter
	@Setter
	public static class Template {

		/**
		 * Default topic to which messages are sent.
		 */
		private String defaultTopic;
	}

	@Getter
	@Setter
	public static class Listener {

		public enum Type {

			/**
			 * Invokes the endpoint with one ConsumerRecord at a time.
			 */
			SINGLE,

			/**
			 * Invokes the endpoint with a batch of ConsumerRecords.
			 */
			BATCH
		}

		/**
		 * Listener type.
		 */
		private Listener.Type type = Listener.Type.SINGLE;

		/**
		 * Listener AckMode. See the spring-kafka documentation.
		 */
		private ContainerProperties.AckMode ackMode;

		/**
		 * Prefix for the listener's consumer client.id property.
		 */
		private String clientId;

		/**
		 * Number of threads to run in the listener containers.
		 */
		private Integer concurrency;

		/**
		 * Timeout to use when polling the consumer.
		 */
		private Duration pollTimeout;

		/**
		 * Multiplier applied to "pollTimeout" to determine if a consumer is
		 * non-responsive.
		 */
		private Float noPollThreshold;

		/**
		 * Number of records between offset commits when ackMode is "COUNT" or
		 * "COUNT_TIME".
		 */
		private Integer ackCount;

		/**
		 * Time between offset commits when ackMode is "TIME" or "COUNT_TIME".
		 */
		private Duration ackTime;

		/**
		 * Sleep interval between Consumer.poll(Duration) calls.
		 */
		private Duration idleBetweenPolls = Duration.ZERO;

		/**
		 * Time between publishing idle consumer events (no data received).
		 */
		private Duration idleEventInterval;

		/**
		 * Time between checks for non-responsive consumers. If a duration suffix is not
		 * specified, seconds will be used.
		 */
		@DurationUnit(ChronoUnit.SECONDS)
		private Duration monitorInterval;

		/**
		 * Whether to log the container configuration during initialization (INFO level).
		 */
		private Boolean logContainerConfig;

		/**
		 * Whether the container should fail to start if at least one of the configured
		 * topics are not present on the broker.
		 */
		private boolean missingTopicsFatal = false;
	}

	@Getter
	@Setter
	public static class Ssl {

		/**
		 * Password of the private key in the key store file.
		 */
		private String keyPassword;

		/**
		 * Location of the key store file.
		 */
		private Resource keyStoreLocation;

		/**
		 * Store password for the key store file.
		 */
		private String keyStorePassword;

		/**
		 * Type of the key store.
		 */
		private String keyStoreType;

		/**
		 * Location of the trust store file.
		 */
		private Resource trustStoreLocation;

		/**
		 * Store password for the trust store file.
		 */
		private String trustStorePassword;

		/**
		 * Type of the trust store.
		 */
		private String trustStoreType;

		/**
		 * SSL protocol to use.
		 */
		private String protocol;

		public Map<String, Object> buildProperties() {
			Properties properties = new Properties();
			PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
			map.from(this::getKeyPassword).to(properties.in(SslConfigs.SSL_KEY_PASSWORD_CONFIG));
			map.from(this::getKeyStoreLocation).as(this::resourceToPath)
				.to(properties.in(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG));
			map.from(this::getKeyStorePassword).to(properties.in(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG));
			map.from(this::getKeyStoreType).to(properties.in(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG));
			map.from(this::getTrustStoreLocation).as(this::resourceToPath)
				.to(properties.in(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG));
			map.from(this::getTrustStorePassword).to(properties.in(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG));
			map.from(this::getTrustStoreType).to(properties.in(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG));
			map.from(this::getProtocol).to(properties.in(SslConfigs.SSL_PROTOCOL_CONFIG));
			return properties;
		}

		private String resourceToPath(Resource resource) {
			try {
				return resource.getFile().getAbsolutePath();
			}
			catch (IOException ex) {
				throw new IllegalStateException("Resource '" + resource + "' must be on a file system", ex);
			}
		}
	}

	@Getter
	@Setter
	public static class Jaas {

		/**
		 * Whether to enable JAAS configuration.
		 */
		private boolean enabled;

		/**
		 * Login module.
		 */
		private String loginModule = "com.sun.security.auth.module.Krb5LoginModule";

		/**
		 * Control flag for login configuration.
		 */
		private KafkaJaasLoginModuleInitializer.ControlFlag controlFlag = KafkaJaasLoginModuleInitializer.ControlFlag.REQUIRED;

		/**
		 * Additional JAAS options.
		 */
		private final Map<String, String> options = new HashMap<>();
	}

	@Getter
	@Setter
	public static class Security {

		/**
		 * Security protocol used to communicate with brokers.
		 */
		private String protocol;

		public Map<String, Object> buildProperties() {
			Properties properties = new Properties();
			PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
			map.from(this::getProtocol).to(properties.in(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG));
			return properties;
		}
	}

	@Getter
	@Setter
	public static class Cleanup {

		/**
		 * Cleanup the application’s local state directory on startup.
		 */
		private boolean onStartup = false;

		/**
		 * Cleanup the application’s local state directory on shutdown.
		 */
		private boolean onShutdown = true;
	}

	@Getter
	public enum IsolationLevel {

		/**
		 * Read everything including aborted transactions.
		 */
		READ_UNCOMMITTED((byte) 0),

		/**
		 * Read records from committed transactions, in addition to records not part of
		 * transactions.
		 */
		READ_COMMITTED((byte) 1);

		private final byte id;

		IsolationLevel(byte id) {
			this.id = id;
		}
	}

	@SuppressWarnings("serial")
	private static class Properties extends HashMap<String, Object> {

		<V> java.util.function.Consumer<V> in(String key) {
			return (value) -> put(key, value);
		}

		Properties with(Ssl ssl, Security security, Map<String, String> properties) {
			putAll(ssl.buildProperties());
			putAll(security.buildProperties());
			putAll(properties);
			return this;
		}
	}
}
