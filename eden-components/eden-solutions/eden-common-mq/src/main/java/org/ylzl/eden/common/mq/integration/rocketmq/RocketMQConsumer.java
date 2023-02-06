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

package org.ylzl.eden.common.mq.integration.rocketmq;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.common.mq.MessageQueueConsumer;
import org.ylzl.eden.common.mq.MessageQueueListener;
import org.ylzl.eden.common.mq.consumer.MessageConsumeException;
import org.ylzl.eden.common.mq.integration.rocketmq.config.RocketMQConfig;
import org.ylzl.eden.common.mq.model.Message;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * RocketMQ 消费者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class RocketMQConsumer implements InitializingBean, DisposableBean {

	private static final String INITIALIZING_ROCKETMQ_CONSUMER = "Initializing RocketMQConsumer";

	private static final String DESTROY_ROCKETMQ_CONSUMER = "Destroy RocketMQConsumer";

	private static final String ROCKETMQ_CONSUMER_CONSUME_ERROR = "RocketMQConsumer consume error: {}";

	private static final String CREATE_DEFAULT_MQPUSH_CONSUMER_GROUP_NAMESPACE_TOPIC = "Create DefaultMQPushConsumer, group: {}, namespace: {}, topic: {}";

	@Getter
	private final Map<String, DefaultMQPushConsumer> consumers = Maps.newConcurrentMap();

	private final RocketMQConfig rocketMQConfig;

	private final List<MessageQueueConsumer> messageQueueConsumers;

	private final Function<String, Boolean> matcher;

	@Override
	public void afterPropertiesSet() {
		log.debug(INITIALIZING_ROCKETMQ_CONSUMER);
		if (CollectionUtils.isEmpty(messageQueueConsumers)) {
			return;
		}
		try {
			for (MessageQueueConsumer messageQueueConsumer : messageQueueConsumers) {
				DefaultMQPushConsumer consumer = createConsumer(messageQueueConsumer);
				if (consumer == null) {
					continue;
				}

				consumer.registerMessageListener((MessageListenerConcurrently) (messageExts, context) -> {
					AtomicReference<ConsumeConcurrentlyStatus> status = new AtomicReference<>(ConsumeConcurrentlyStatus.RECONSUME_LATER);
					List<Message> messages = Lists.newArrayListWithCapacity(messageExts.size());
					messageExts.forEach(messageExt -> messages.add(
						Message.builder()
							.topic(messageExt.getTopic())
							.partition(messageExt.getQueueId())
							.key(messageExt.getKeys())
							.tags(messageExt.getTags())
							.delayTimeLevel(messageExt.getDelayTimeLevel())
							.body(new String(messageExt.getBody()))
							.build()));

					messageQueueConsumer.consume(messages,
						() -> {
							if (status.get() != ConsumeConcurrentlyStatus.CONSUME_SUCCESS) {
								status.set(ConsumeConcurrentlyStatus.CONSUME_SUCCESS);
							}
						}
					);
					return status.get();
				});
				consumer.start();
			}
		} catch (MQClientException e) {
			log.error(ROCKETMQ_CONSUMER_CONSUME_ERROR, e.getMessage(), e);
			throw new MessageConsumeException(e.getMessage());
		}
	}

	@Override
	public void destroy() {
		log.debug(DESTROY_ROCKETMQ_CONSUMER);
		consumers.forEach((k, v) -> {
			v.shutdown();
		});
	}

	private DefaultMQPushConsumer createConsumer(MessageQueueConsumer messageQueueConsumer) throws MQClientException {
		Class<? extends MessageQueueConsumer> clazz = messageQueueConsumer.getClass();
		MessageQueueListener annotation = clazz.getAnnotation(MessageQueueListener.class);
		if (!matcher.apply(annotation.type())) {
			return null;
		}

		String namespace = null;
		if (StringUtils.isNotBlank(rocketMQConfig.getConsumer().getNamespace())) {
			namespace = rocketMQConfig.getConsumer().getNamespace();
		}

		String topic = null;
		if (StringUtils.isNotBlank(annotation.topic())) {
			topic = annotation.topic();
		} else if (StringUtils.isNotBlank(rocketMQConfig.getConsumer().getTopic())) {
			topic = rocketMQConfig.getConsumer().getTopic();
		}

		String group = null;
		if (StringUtils.isNotBlank(annotation.group())) {
			group = annotation.group();
		} else if (StringUtils.isNotBlank(rocketMQConfig.getConsumer().getGroup())) {
			group = rocketMQConfig.getConsumer().getGroup() + Strings.UNDERLINE + topic;
		}

		String selectorExpression = null;
		if (StringUtils.isNotBlank(annotation.selectorExpression())) {
			selectorExpression = annotation.selectorExpression();
		} else if (StringUtils.isNotBlank(rocketMQConfig.getConsumer().getSelectorExpression())) {
			selectorExpression = rocketMQConfig.getConsumer().getSelectorExpression();
		}

		RPCHook rpcHook = null;
		if (StringUtils.isNotBlank(rocketMQConfig.getConsumer().getAccessKey()) &&
			StringUtils.isNotBlank(rocketMQConfig.getConsumer().getSecretKey())) {
			rpcHook = new AclClientRPCHook(new SessionCredentials(
				rocketMQConfig.getConsumer().getAccessKey(),
				rocketMQConfig.getConsumer().getSecretKey()));
		}

		int pullBatchSize = 32;
		if (annotation.pullBatchSize() > 0) {
			pullBatchSize = annotation.pullBatchSize();
		} else if (rocketMQConfig.getConsumer().getPullBatchSize() > 0) {
			pullBatchSize = rocketMQConfig.getConsumer().getPullBatchSize();
		}

		int consumeMessageBatchMaxSize = 1;
		if (annotation.consumeMessageBatchMaxSize() > 0) {
			consumeMessageBatchMaxSize = annotation.consumeMessageBatchMaxSize();
		}

		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(namespace, group, rpcHook);
		consumer.setNamesrvAddr(rocketMQConfig.getNameServer());
		consumer.subscribe(topic, selectorExpression);
		consumer.setPullBatchSize(pullBatchSize);
		consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
		consumer.setMessageModel(MessageModel.valueOf(rocketMQConfig.getConsumer().getMessageModel()));

		log.debug(CREATE_DEFAULT_MQPUSH_CONSUMER_GROUP_NAMESPACE_TOPIC, group, namespace, topic);
		consumers.put(topic, consumer);
		return consumer;
	}
}
