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

package org.ylzl.eden.common.mq.spring.boot.env;

import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.common.topic.TopicValidator;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 完善 RocketMQ 4.7.x 版本的配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @link https://github.com/apache/rocketmq-spring/blob/master/rocketmq-spring-boot/src/main/java/org/apache/rocketmq/spring/autoconfigure/RocketMQProperties.java
 * @since 2.4.13
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "rocketmq.consumer")
public class RocketMQConsumerProperties {

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
