package org.ylzl.eden.spring.integration.messagequeue.rocketmq;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.spring.integration.messagequeue.annotation.MessageQueueListener;
import org.ylzl.eden.spring.integration.messagequeue.consumer.MessageListener;
import org.ylzl.eden.spring.integration.messagequeue.consumer.MessageQueueConsumer;
import org.ylzl.eden.spring.integration.messagequeue.consumer.MessageQueueConsumerException;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * RocketMQ 消费者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class RocketMQConsumer implements MessageQueueConsumer, InitializingBean, DisposableBean {

	private static final String INITIALIZING_ROCKETMQ_CONSUMER = "Initializing RocketMQConsumer";

	private static final String DESTROY_ROCKETMQ_CONSUMER = "Destroy RocketMQConsumer";

	private static final String ROCKETMQ_CONSUMER_CONSUME_ERROR = "RocketMQConsumer consume error: {}";

	private final RocketMQProperties rocketMQProperties;

	private final List<MessageListener> messageListeners;

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug(INITIALIZING_ROCKETMQ_CONSUMER);
		consume();
	}

	@Override
	public void destroy() throws Exception {
		log.debug(DESTROY_ROCKETMQ_CONSUMER);
	}

	@Override
	public void consume() throws MessageQueueConsumerException {
		if (CollectionUtils.isEmpty(messageListeners)) {
			return;
		}
		try {
			for (MessageListener listener : messageListeners) {
				Class<? extends MessageListener> clazz = listener.getClass();
				MessageQueueListener annotation = clazz.getAnnotation(MessageQueueListener.class);
				DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(annotation.consumerGroup());
				if (StringUtils.isNotBlank(annotation.namespace())) {
					consumer.setNamespace(annotation.namespace());
				}
				consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
				consumer.subscribe(annotation.topic(), annotation.tags());
				consumer.registerMessageListener((MessageListenerConcurrently) (messageExts, context) -> {
					AtomicReference<ConsumeConcurrentlyStatus> status =
						new AtomicReference<>(ConsumeConcurrentlyStatus.RECONSUME_LATER);
					List<String> messages = Lists.newArrayListWithCapacity(messageExts.size());
					messageExts.forEach(messageExt -> messages.add(new String(messageExt.getBody())));
					listener.consume(messages,
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
			throw new MessageQueueConsumerException(e.getMessage());
		}
	}
}
