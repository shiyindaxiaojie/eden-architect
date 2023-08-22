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

	private String group;

	private String topic;

	private String messageModel = "CLUSTERING";

	private String selectorType = "TAG";

	private String selectorExpression = "*";

	private String accessKey;

	private String secretKey;

	private int pullBatchSize = 32;

	private boolean enableMsgTrace = false;

	private String customizedTraceTopic = TopicValidator.RMQ_SYS_TRACE_TOPIC;

	/** 旧版本的 Spring Boot Starter 没有提供以下配置 */

	private String namespace;

	/** 以下是官方没有暴露的配置 */

	private String consumeMode = "CONCURRENTLY";

	private int pullThresholdForQueue = 1000;

	private int pullThresholdSizeForQueue = 100;

	private int consumeMessageBatchMaxSize = 1;

	private int consumeConcurrentlyMaxSpan = 2000;

	private int consumeThreadMin = 20;

	private int consumeThreadMax = 64;

	private long consumeTimeout = 15L;

	private long suspendCurrentQueueTimeMillis = 1000;

	private int delayLevelWhenNextConsume = 0;
}
