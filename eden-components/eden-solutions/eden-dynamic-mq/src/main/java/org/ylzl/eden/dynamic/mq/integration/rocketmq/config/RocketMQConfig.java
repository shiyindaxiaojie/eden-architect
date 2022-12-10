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

package org.ylzl.eden.dynamic.mq.integration.rocketmq.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.common.topic.TopicValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * RocketMQ 配置
 * <p>完善 RocketMQ 4.7.x 版本的配置</p>
 * @link 官方配置：<a href="https://github.com/apache/rocketmq-spring/blob/master/rocketmq-spring-boot/src/main/java/org/apache/rocketmq/spring/autoconfigure/RocketMQProperties.java">...</a>
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
@Setter
public class RocketMQConfig {

	/**
	 * The name server for rocketMQ, formats: `host:port;host:port`.
	 */
	private String nameServer;

	/**
	 * Enum type for accessChannel, values: LOCAL, CLOUD
	 */
	private String accessChannel;

	private Producer producer = new Producer();

	private Consumer consumer = new Consumer();

	@Getter
	@Setter
	public static class Producer {

		/**
		 * Group name of producer.
		 */
		private String group;

		/**
		 * Millis of send message timeout.
		 */
		private int sendMessageTimeout = 3000;

		/**
		 * Compress message body threshold, namely, message body larger than 4k will be compressed on default.
		 */
		private int compressMessageBodyThreshold = 1024 * 4;

		/**
		 * Maximum number of retry to perform internally before claiming sending failure in synchronous mode.
		 * This may potentially cause message duplication which is up to application developers to resolve.
		 */
		private int retryTimesWhenSendFailed = 2;

		/**
		 * <p> Maximum number of retry to perform internally before claiming sending failure in asynchronous mode. </p>
		 * This may potentially cause message duplication which is up to application developers to resolve.
		 */
		private int retryTimesWhenSendAsyncFailed = 2;

		/**
		 * Indicate whether to retry another broker on sending failure internally.
		 */
		private boolean retryNextServer = false;

		/**
		 * Maximum allowed message size in bytes.
		 */
		private int maxMessageSize = 1024 * 1024 * 4;

		/**
		 * The property of "access-key".
		 */
		private String accessKey;

		/**
		 * The property of "secret-key".
		 */
		private String secretKey;

		/**
		 * Switch flag instance for message trace.
		 */
		private boolean enableMsgTrace = true;

		/**
		 * The name value of message trace topic.If you don't config,you can use the default trace topic name.
		 */
		private String customizedTraceTopic = TopicValidator.RMQ_SYS_TRACE_TOPIC;

		/**
		 * Namespace for this MQ Producer instance.
		 */
		private String namespace;
	}

	@Getter
	@Setter
	public static final class Consumer {

		/**
		 * listener configuration container
		 * the pattern is like this:
		 * group1.topic1 = false
		 * group2.topic2 = true
		 * group3.topic3 = false
		 */
		private Map<String, Map<String, Boolean>> listeners = new HashMap<>();

		/**
		 * Group name of consumer.
		 */
		private String group;

		/**
		 * Namespace for this MQ Consumer instance.
		 */
		private String namespace;

		/**
		 * Topic name of consumer.
		 */
		private String topic;

		/**
		 * Control message mode, if you want all subscribers receive message all message, broadcasting is a good choice.
		 */
		private String messageModel = "CLUSTERING";

		/**
		 * Control how to selector message.
		 */
		private String selectorType = "TAG";

		/**
		 * Control which message can be select.
		 */
		private String selectorExpression = "*";

		/**
		 * The property of "access-key".
		 */
		private String accessKey;

		/**
		 * The property of "secret-key".
		 */
		private String secretKey;

		/**
		 * Maximum number of messages pulled each time.
		 */
		private int pullBatchSize = 10;

		/**
		 * Switch flag instance for message trace.
		 */
		private boolean enableMsgTrace = false;

		/**
		 * The name value of message trace topic.If you don't config,you can use the default trace topic name.
		 */
		private String customizedTraceTopic = TopicValidator.RMQ_SYS_TRACE_TOPIC;
	}
}
