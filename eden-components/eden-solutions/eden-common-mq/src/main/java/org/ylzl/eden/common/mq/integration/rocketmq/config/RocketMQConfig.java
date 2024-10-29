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

package org.ylzl.eden.common.mq.integration.rocketmq.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.rocketmq.common.topic.TopicValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * RocketMQ 配置
 * <p>完善 RocketMQ 4.7.x 版本的配置</p>
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @link 官方配置：<a href="https://github.com/apache/rocketmq-spring/blob/master/rocketmq-spring-boot/src/main/java/org/apache/rocketmq/spring/autoconfigure/RocketMQProperties.java">...</a>
 * @since 2.4.x
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class RocketMQConfig {

	private String nameServer;

	private String accessChannel;

	private Producer producer = new Producer();

	private Consumer consumer = new Consumer();

	@Getter
	@Setter
	public static class Producer {

		private String group;

		private String namespace;

		private int sendMessageTimeout = 3000;

		private int compressMessageBodyThreshold = 1024 * 4;

		private int retryTimesWhenSendFailed = 2;

		private int retryTimesWhenSendAsyncFailed = 2;

		private boolean retryNextServer = false;

		private int maxMessageSize = 1024 * 1024 * 4;

		private String accessKey;

		private String secretKey;

		private boolean enableMsgTrace = true;

		private String customizedTraceTopic = TopicValidator.RMQ_SYS_TRACE_TOPIC;
	}

	@Getter
	@Setter
	public static final class Consumer {

		private Map<String, Map<String, Boolean>> listeners = new HashMap<>();

		private String group;

		private String namespace;

		private String topic;

		private String consumeMode = "CONCURRENTLY";

		private String messageModel = "CLUSTERING";

		private String selectorType = "TAG";

		private String selectorExpression = "*";

		private String accessKey;

		private String secretKey;

		private int pullThresholdForQueue = 1000;

		private int pullThresholdSizeForQueue = 100;

		private int pullBatchSize = 32;

		private int consumeMessageBatchMaxSize = 1;

		private int consumeConcurrentlyMaxSpan = 2000;

		private int consumeThreadMin = 20;

		private int consumeThreadMax = 64;

		private long consumeTimeout = 15L;

		private boolean enableMsgTrace = false;

		private String customizedTraceTopic = TopicValidator.RMQ_SYS_TRACE_TOPIC;

		private long suspendCurrentQueueTimeMillis = 1000;

		private int delayLevelWhenNextConsume = 0;
	}
}
