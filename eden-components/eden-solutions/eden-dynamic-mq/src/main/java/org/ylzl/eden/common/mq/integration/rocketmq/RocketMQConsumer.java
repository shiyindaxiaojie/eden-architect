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
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.filter.ExpressionType;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.ylzl.eden.common.mq.MessageQueueConsumer;
import org.ylzl.eden.common.mq.MessageQueueListener;
import org.ylzl.eden.common.mq.consumer.ConsumeMode;
import org.ylzl.eden.common.mq.consumer.MessageConsumeException;
import org.ylzl.eden.common.mq.integration.rocketmq.config.RocketMQConfig;
import org.ylzl.eden.common.mq.model.Message;
import org.ylzl.eden.commons.lang.Strings;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public class RocketMQConsumer implements InitializingBean, DisposableBean, ApplicationContextAware {

	private static final String INITIALIZING_ROCKETMQ_CONSUMER = "Initializing RocketMQConsumer";

	private static final String DESTROY_ROCKETMQ_CONSUMER = "Destroy RocketMQConsumer";

	private static final String ROCKETMQ_CONSUMER_CONSUME_ERROR = "RocketMQConsumer consume error: {}";

	private static final String CREATE_MQ_CONSUMER = "Create DefaultMQPushConsumer, group: {}, namespace: {}, topic: {}";

	@Getter
	private final Map<String, DefaultMQPushConsumer> consumers = Maps.newConcurrentMap();

	private final RocketMQConfig rocketMQConfig;

	private final List<MessageQueueConsumer> messageQueueConsumers;

	private final Function<String, Boolean> matcher;

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() {
		log.debug(INITIALIZING_ROCKETMQ_CONSUMER);
		if (CollectionUtils.isEmpty(messageQueueConsumers)) {
			return;
		}
		for (MessageQueueConsumer messageQueueConsumer : messageQueueConsumers) {
			DefaultMQPushConsumer consumer;
			try {
				consumer = initRocketMQPushConsumer(messageQueueConsumer);
			} catch (MQClientException e) {
				throw new RuntimeException(e);
			}
			if (consumer == null) {
				continue;
			}

			try {
				consumer.start();
			} catch (MQClientException e) {
				log.error(ROCKETMQ_CONSUMER_CONSUME_ERROR, e.getMessage(), e);
				throw new MessageConsumeException(e.getMessage());
			}
		}
	}

	@Override
	public void destroy() {
		log.debug(DESTROY_ROCKETMQ_CONSUMER);
		consumers.forEach((k, v) -> v.shutdown());
		consumers.clear();
	}

	private DefaultMQPushConsumer initRocketMQPushConsumer(MessageQueueConsumer messageQueueConsumer) throws MQClientException {
		Class<? extends MessageQueueConsumer> clazz = messageQueueConsumer.getClass();
		MessageQueueListener annotation = clazz.getAnnotation(MessageQueueListener.class);
		if (!matcher.apply(annotation.type())) {
			return null;
		}

		// 命名空间
		String namespace = null;
		if (StringUtils.isNotBlank(rocketMQConfig.getConsumer().getNamespace())) {
			namespace = rocketMQConfig.getConsumer().getNamespace();
		}

		// 主题
		String topic = null;
		if (StringUtils.isNotBlank(annotation.topic())) {
			topic = annotation.topic();
		} else if (StringUtils.isNotBlank(rocketMQConfig.getConsumer().getTopic())) {
			topic = rocketMQConfig.getConsumer().getTopic();
		}
		AssertUtils.notNull(topic, "PROP-REQUIRED-500", "rocketmq.consumer.topic");

		// 消费组
		String consumerGroup = null;
		if (StringUtils.isNotBlank(annotation.group())) {
			consumerGroup = annotation.group();
		} else if (StringUtils.isNotBlank(rocketMQConfig.getConsumer().getGroup())) {
			consumerGroup = rocketMQConfig.getConsumer().getGroup() + Strings.UNDERLINE + topic;
		}
		AssertUtils.notNull(consumerGroup, "PROP-REQUIRED-500", "rocketmq.consumer.group");

		// 初始化消费者
		Environment environment = this.applicationContext.getEnvironment();
		RPCHook rpcHook = RocketMQUtil.getRPCHookByAkSk(applicationContext.getEnvironment(),
			rocketMQConfig.getConsumer().getAccessKey(), rocketMQConfig.getConsumer().getSecretKey());
		boolean enableMsgTrace = annotation.enableMsgTrace();
		DefaultMQPushConsumer consumer;
		if (Objects.nonNull(rpcHook)) {
			consumer = new DefaultMQPushConsumer(namespace, consumerGroup, rpcHook, new AllocateMessageQueueAveragely(),
				enableMsgTrace, environment.resolveRequiredPlaceholders(topic));
			consumer.setVipChannelEnabled(false);
			consumer.setInstanceName(RocketMQUtil.getInstanceName(rpcHook, consumerGroup));
		} else {
			log.warn("RocketMQ access-key or secret-key not configure in {}.", this.getClass().getName());
			consumer = new DefaultMQPushConsumer(namespace, consumerGroup, null, new AllocateMessageQueueAveragely(),
				enableMsgTrace, environment.resolveRequiredPlaceholders(topic));
		}
		consumer.setNamesrvAddr(rocketMQConfig.getNameServer());

		// 消费者本地缓存消息数，超过这个阈值会降低消费速率
		int pullThresholdForQueue = 1000;
		if (rocketMQConfig.getConsumer().getPullThresholdForQueue() > 0) {
			pullThresholdForQueue = rocketMQConfig.getConsumer().getPullThresholdForQueue();
		}
		consumer.setPullThresholdForQueue(pullThresholdForQueue);

		// 消费者本地缓存消息大小，超过这个阈值会降低消费速率
		int pullThresholdSizeForQueue = 100;
		if (rocketMQConfig.getConsumer().getPullThresholdSizeForQueue() > 0) {
			pullThresholdSizeForQueue = rocketMQConfig.getConsumer().getPullThresholdSizeForQueue();
		}
		consumer.setPullThresholdSizeForQueue(pullThresholdSizeForQueue);

		// 批量拉取消息条数
		int pullBatchSize = 32;
		if (annotation.pullBatchSize() > 0) {
			pullBatchSize = annotation.pullBatchSize();
		} else if (rocketMQConfig.getConsumer().getPullBatchSize() > 0) {
			pullBatchSize = rocketMQConfig.getConsumer().getPullBatchSize();
		}
		consumer.setPullBatchSize(pullBatchSize);

		// 批量消费消息条数
		int consumeMessageBatchMaxSize = 1;
		if (annotation.consumeMessageBatchMaxSize() > 0) {
			consumeMessageBatchMaxSize = annotation.consumeMessageBatchMaxSize();
		} else if (rocketMQConfig.getConsumer().getConsumeMessageBatchMaxSize() > 0) {
			consumeMessageBatchMaxSize = rocketMQConfig.getConsumer().getConsumeMessageBatchMaxSize();
		}
		consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);

		// 消费者本地缓存消息跨度，超过这个阈值会降低消费速率
		int consumeConcurrentlyMaxSpan = 2000;
		if (rocketMQConfig.getConsumer().getConsumeConcurrentlyMaxSpan() > 0) {
			consumeConcurrentlyMaxSpan = rocketMQConfig.getConsumer().getConsumeConcurrentlyMaxSpan();
		}
		consumer.setConsumeConcurrentlyMaxSpan(consumeConcurrentlyMaxSpan);

		// 消费最大线程
		int consumeThreadMax = 64;
		if (annotation.consumeThreadMax() > 0) {
			consumeThreadMax = annotation.consumeThreadMax();
		} else if (rocketMQConfig.getConsumer().getConsumeThreadMax() > 0) {
			consumeThreadMax = rocketMQConfig.getConsumer().getConsumeThreadMax();
		}
		consumer.setConsumeThreadMax(consumeThreadMax);

		// 消费最小线程
		int consumeThreadMin = 1;
		if (annotation.consumeThreadMin() > 0) {
			consumeThreadMin = annotation.consumeThreadMin();
		} else if (rocketMQConfig.getConsumer().getConsumeThreadMax() > 0) {
			consumeThreadMin = rocketMQConfig.getConsumer().getConsumeThreadMin();
		}
        consumer.setConsumeThreadMin(Math.min(consumeThreadMin, consumeThreadMax));

		// 消费超时
		if (annotation.consumeTimeout() > 0) {
			consumer.setConsumeTimeout(annotation.consumeTimeout());
		} else if (rocketMQConfig.getConsumer().getConsumeTimeout() > 0) {
			consumer.setConsumeTimeout(rocketMQConfig.getConsumer().getConsumeTimeout());
		}

		// 消息模式
		switch (annotation.messageModel()) {
			case BROADCASTING:
				consumer.setMessageModel(MessageModel.BROADCASTING);
				break;
			case CLUSTERING:
				consumer.setMessageModel(MessageModel.CLUSTERING);
				break;
			default:
				String messageModel = rocketMQConfig.getConsumer().getMessageModel();
				AssertUtils.notNull(messageModel, "PROP-REQUIRED-500", "rocketmq.consumer.messageModel");
				consumer.setMessageModel(MessageModel.valueOf(rocketMQConfig.getConsumer().getMessageModel()));
		}

		// 消息模式
		String selectorExpression = annotation.selectorExpression();
		AssertUtils.notNull(selectorExpression, "PROP-REQUIRED-500", "rocketmq.consumer.selectorType");
		MessageSelector messageSelector;
		switch (annotation.selectorType()) {
			case TAG:
				messageSelector = MessageSelector.byTag(selectorExpression);
				break;
			case SQL92:
				messageSelector = MessageSelector.bySql(selectorExpression);
				break;
			default:
				messageSelector = ExpressionType.isTagType(rocketMQConfig.getConsumer().getSelectorType()) ?
					MessageSelector.byTag(selectorExpression) : MessageSelector.bySql(selectorExpression);
				consumer.setMessageModel(MessageModel.valueOf(rocketMQConfig.getConsumer().getMessageModel()));
		}
		consumer.subscribe(topic, messageSelector);

		// 设置顺序模式或者并发模式
		ConsumeMode consumeMode = annotation.consumeMode() != ConsumeMode.UNSET ?
			annotation.consumeMode() : ConsumeMode.valueOf(rocketMQConfig.getConsumer().getConsumeMode());
		switch (consumeMode) {
			case ORDERLY:
				consumer.setMessageListener(new DefaultMessageListenerOrderly(messageQueueConsumer));
				break;
			case CONCURRENTLY:
				consumer.setMessageListener(new DefaultMessageListenerConcurrently(messageQueueConsumer));
				break;
			default:
				throw new IllegalArgumentException("Property 'consumeMode' was wrong.");
		}

		log.debug(CREATE_MQ_CONSUMER, consumerGroup, namespace, topic);
		consumers.put(topic, consumer);
		return consumer;
	}

	@RequiredArgsConstructor
	public class DefaultMessageListenerConcurrently implements MessageListenerConcurrently {

		private final MessageQueueConsumer messageQueueConsumer;

		@Override
		public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeConcurrentlyContext context) {
			AtomicReference<ConsumeConcurrentlyStatus> status =
				new AtomicReference<>(ConsumeConcurrentlyStatus.RECONSUME_LATER);
			List<Message> messages = getMessages(messageExts);
			long now = System.currentTimeMillis();
			try {
				messageQueueConsumer.consume(messages, () -> status.set(ConsumeConcurrentlyStatus.CONSUME_SUCCESS));
				long costTime = System.currentTimeMillis() - now;
				log.debug("consume message concurrently cost {} ms, message: {}", costTime, messageExts);
			} catch (Exception e) {
				log.warn("consume message concurrently failed, message: {}", messageExts, e);
				context.setDelayLevelWhenNextConsume(rocketMQConfig.getConsumer().getDelayLevelWhenNextConsume());
			}
			return status.get();
		}
	}

	@RequiredArgsConstructor
	public class DefaultMessageListenerOrderly implements MessageListenerOrderly {

		public final MessageQueueConsumer messageQueueConsumer;

		@Override
		public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeOrderlyContext context) {
			AtomicReference<ConsumeOrderlyStatus> status =
				new AtomicReference<>(ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT);
			List<Message> messages = getMessages(messageExts);
			long now = System.currentTimeMillis();
			try {
				messageQueueConsumer.consume(messages, () -> status.set(ConsumeOrderlyStatus.SUCCESS));
				long costTime = System.currentTimeMillis() - now;
				log.debug("consume message concurrently cost {} ms, message: {}", costTime, messageExts);
			} catch (Exception e) {
				log.warn("consume message concurrently failed, message: {}", messageExts, e);
				context.setSuspendCurrentQueueTimeMillis(rocketMQConfig.getConsumer().getSuspendCurrentQueueTimeMillis());
			}
			return status.get();
		}
	}

	@NotNull
	private static List<Message> getMessages(List<MessageExt> messageExts) {
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
		return messages;
	}
}
