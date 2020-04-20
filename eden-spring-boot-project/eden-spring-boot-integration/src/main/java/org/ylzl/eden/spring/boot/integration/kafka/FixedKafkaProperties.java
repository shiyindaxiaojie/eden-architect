/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.boot.integration.kafka;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.kafka.listener.AbstractMessageListenerContainer.AckMode;
import org.springframework.util.CollectionUtils;
import org.ylzl.eden.spring.boot.framework.core.FrameworkConstants;

import java.io.IOException;
import java.util.*;

/**
 * Kafka 配置属性
 *
 * @author gyl
 * @since 0.0.1
 */
@Getter
@Setter
@ConfigurationProperties(prefix = FrameworkConstants.PROP_SPRING_PREFIX + ".kafka")
public class FixedKafkaProperties {

	private Boolean enabled;

	private List<String> bootstrapServers = new ArrayList<>(Collections.singletonList("localhost:9092"));

	private String clientId;

	private final Map<String, String> properties = new HashMap<>();

	private final Consumer consumer = new Consumer();

	private final Producer producer = new Producer();

	private final Listener listener = new Listener();

	private final Ssl ssl = new Ssl();

	private final Template template = new Template();

	public Map<String, Object> buildConsumerProperties() {
		Map<String, Object> properties = buildCommonProperties();
		properties.putAll(this.consumer.buildProperties());
		return properties;
	}

	public Map<String, Object> buildProducerProperties() {
		Map<String, Object> properties = buildCommonProperties();
		properties.putAll(this.producer.buildProperties());
		return properties;
	}

	private Map<String, Object> buildCommonProperties() {
		Map<String, Object> properties = new HashMap<>();
		if (this.bootstrapServers != null) {
			properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
		}
		if (this.clientId != null) {
			properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, this.clientId);
		}
		if (this.ssl.getKeyPassword() != null) {
			properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, this.ssl.getKeyPassword());
		}
		if (this.ssl.getKeystoreLocation() != null) {
			properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, resourceToPath(this.ssl.getKeystoreLocation()));
		}
		if (this.ssl.getKeystorePassword() != null) {
			properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, this.ssl.getKeystorePassword());
		}
		if (this.ssl.getTruststoreLocation() != null) {
			properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, resourceToPath(this.ssl.getTruststoreLocation()));
		}
		if (this.ssl.getTruststorePassword() != null) {
			properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, this.ssl.getTruststorePassword());
		}
		if (!CollectionUtils.isEmpty(this.properties)) {
			properties.putAll(this.properties);
		}
		return properties;
	}

	private static String resourceToPath(Resource resource) {
		try {
			return resource.getFile().getAbsolutePath();
		} catch (IOException ex) {
			throw new IllegalStateException("Resource '" + resource + "' must be on a file system", ex);
		}
	}

	@Getter
	@Setter
	public static class Consumer {

		private final Ssl ssl = new Ssl();

		private Integer autoCommitInterval;

		private String autoOffsetReset;

		private List<String> bootstrapServers;

		private String clientId;

		private Integer connectionsMaxIdleMs;

		private Boolean enableAutoCommit;

		private Integer fetchMaxWait;

		private Integer fetchMinSize;

		private String groupId;

		private Integer heartbeatInterval;

		private Class<?> keyDeserializer = StringDeserializer.class;

		private Integer maxPollRecords;

		private Integer reconnectBackoffMs;

		private Integer requestTimeoutMs;

		private Integer retryBackoffMs;

		private Class<?> valueDeserializer = StringDeserializer.class;

		private final Map<String, String> properties = new HashMap<>();

		public Map<String, Object> buildProperties() {
			Map<String, Object> properties = new HashMap<>();
			if (this.autoCommitInterval != null) {
				properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
					this.autoCommitInterval);
			}
			if (this.autoOffsetReset != null) {
				properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
					this.autoOffsetReset);
			}
			if (this.bootstrapServers != null) {
				properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
					this.bootstrapServers);
			}
			if (this.clientId != null) {
				properties.put(ConsumerConfig.CLIENT_ID_CONFIG, this.clientId);
			}
			if (this.connectionsMaxIdleMs != null) {
				properties.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, this.connectionsMaxIdleMs);
			}
			if (this.enableAutoCommit != null) {
				properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
					this.enableAutoCommit);
			}
			if (this.fetchMaxWait != null) {
				properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,
					this.fetchMaxWait);
			}
			if (this.fetchMinSize != null) {
				properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, this.fetchMinSize);
			}
			if (this.groupId != null) {
				properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
			}
			if (this.heartbeatInterval != null) {
				properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, this.heartbeatInterval);
			}
			if (this.keyDeserializer != null) {
				properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.keyDeserializer);
			}
			if (this.maxPollRecords != null) {
				properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
			}
			if (this.reconnectBackoffMs != null) {
				properties.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, this.reconnectBackoffMs);
			}
			if (this.requestTimeoutMs != null) {
				properties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, this.requestTimeoutMs);
			}
			if (this.retryBackoffMs != null) {
				properties.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, this.retryBackoffMs);
			}
			if (this.valueDeserializer != null) {
				properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.valueDeserializer);
			}
			properties.putAll(this.properties);
			return properties;
		}
	}

	@Getter
	@Setter
	public static class Producer {

		private final Ssl ssl = new Ssl();

		private String acks;

		private Integer batchSize;

		private List<String> bootstrapServers;

		private Long bufferMemory;

		private String clientId;

		private String compressionType;

		private Integer lingerMs;

		private Class<?> keySerializer = StringSerializer.class;

		private Integer maxBlockMs;

		private Integer maxRequestSize;

		private Integer retries;

		private Integer reconnectBackoffMs;

		private Integer requestTimeoutMs;

		private Integer retryBackoffMs;

		private Class<?> valueSerializer = StringSerializer.class;

		private final Map<String, String> properties = new HashMap<>();

		public Map<String, Object> buildProperties() {
			Map<String, Object> properties = new HashMap<>();
			if (this.acks != null) {
				properties.put(ProducerConfig.ACKS_CONFIG, this.acks);
			}
			if (this.batchSize != null) {
				properties.put(ProducerConfig.BATCH_SIZE_CONFIG, this.batchSize);
			}
			if (this.bootstrapServers != null) {
				properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
					this.bootstrapServers);
			}
			if (this.bufferMemory != null) {
				properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, this.bufferMemory);
			}
			if (this.clientId != null) {
				properties.put(ProducerConfig.CLIENT_ID_CONFIG, this.clientId);
			}
			if (this.compressionType != null) {
				properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, this.compressionType);
			}
			if (this.lingerMs != null) {
				properties.put(ProducerConfig.LINGER_MS_CONFIG, this.lingerMs);
			}
			if (this.keySerializer != null) {
				properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.keySerializer);
			}
			if (this.maxBlockMs != null) {
				properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, this.maxBlockMs);
			}
			if (this.maxRequestSize != null) {
				properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, this.maxRequestSize);
			}
			if (this.retries != null) {
				properties.put(ProducerConfig.RETRIES_CONFIG, this.retries);
			}
			if (this.reconnectBackoffMs != null) {
				properties.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, this.reconnectBackoffMs);
			}
			if (this.requestTimeoutMs != null) {
				properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, this.requestTimeoutMs);
			}
			if (this.retryBackoffMs != null) {
				properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, this.retryBackoffMs);
			}
			if (this.valueSerializer != null) {
				properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.valueSerializer);
			}
			properties.putAll(this.properties);
			return properties;
		}
	}

	@Getter
	@Setter
	public static class Listener {

		public enum Type {

			SINGLE,
			BATCH;
		}

		private Type type = Type.SINGLE;

		private AckMode ackMode;

		private Integer concurrency;

		private Long pollTimeout;

		private Integer ackCount;

		private Long ackTime;
	}

	@Getter
	@Setter
	public static class Ssl {

		private String keyPassword;

		private Resource keystoreLocation;

		private String keystorePassword;

		private Resource truststoreLocation;

		private String truststorePassword;
	}


	@Getter
	@Setter
	public static class Template {

		private String defaultTopic;
	}
}
