package org.ylzl.eden.spring.integration.messagequeue.kafka;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.ConsumerFactory;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.spring.integration.messagequeue.common.MessageQueueType;
import org.ylzl.eden.spring.integration.messagequeue.core.Message;
import org.ylzl.eden.spring.integration.messagequeue.core.MessageQueueConsumer;
import org.ylzl.eden.spring.integration.messagequeue.core.MessageQueueListener;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * RocketMQ 消费者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer implements InitializingBean, DisposableBean {

	private static final String KAFKA_CONSUMER_PROCESSOR_CONSUME_ERROR = "KafkaConsumerProcessor consume error: {}";

	public static final String INITIALIZING_KAFKA_CONSUMER = "Initializing KafkaConsumer";

	public static final String DESTROY_KAFKA_CONSUMER = "Destroy KafkaConsumer";

	public static final String CREATE_CONSUMER_FROM_CONSUMER_FACTORY_GROUP_TOPIC = "Create consumer from consumerFactory, group: {}, topic: {}";

	private final List<Consumer<String, String>> consumers = Lists.newArrayList();

	private final KafkaProperties kafkaProperties;

	private final List<MessageQueueConsumer> messageQueueConsumers;

	private final ConsumerFactory<String, String> consumerFactory;

	private final TaskExecutor taskExecutor;

	@SuppressWarnings("InfiniteLoopStatement")
	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug(INITIALIZING_KAFKA_CONSUMER);
		if (CollectionUtils.isEmpty(messageQueueConsumers)) {
			return;
		}
		for (MessageQueueConsumer messageQueueConsumer : messageQueueConsumers) {
			Consumer<String, String> consumer = createConsumer(messageQueueConsumer);
			if (consumer == null) {
				continue;
			}
			consumers.add(consumer);
			taskExecutor.execute(() -> {
				while (true) {
					try {
						ConsumerRecords<String, String> consumerRecords =
							consumer.poll(kafkaProperties.getConsumer().getFetchMaxWait());
						if (consumerRecords == null || consumerRecords.isEmpty()) {
							continue;
						}
						int maxPollRecords = kafkaProperties.getConsumer().getMaxPollRecords();
						Map<TopicPartition, OffsetAndMetadata> offsets = Maps.newHashMapWithExpectedSize(maxPollRecords);
						List<Message> messages = Lists.newArrayListWithCapacity(consumerRecords.count());
						consumerRecords.forEach(record -> {
							offsets.put(new TopicPartition(record.topic(), record.partition()),
								new OffsetAndMetadata(record.offset() + 1));

							messages.add(Message.builder()
									.topic(record.topic())
									.partition(record.partition())
									.key(record.key())
									.body(record.value()).build());
						});
						messageQueueConsumer.consume(messages, () -> consumer.commitSync(offsets));
					} catch (Exception e) {
						log.error(KAFKA_CONSUMER_PROCESSOR_CONSUME_ERROR, e.getMessage(), e);
					}
				}
			});
		}
	}

	@Override
	public void destroy() {
		log.debug(DESTROY_KAFKA_CONSUMER);
		consumers.forEach(Consumer::unsubscribe);
	}

	private Consumer<String, String> createConsumer(MessageQueueConsumer messageQueueConsumer) {
		Class<? extends MessageQueueConsumer> clazz = messageQueueConsumer.getClass();
		MessageQueueListener annotation = clazz.getAnnotation(MessageQueueListener.class);

		if (StringUtils.isNotBlank(annotation.messageQueueType()) &&
			!MessageQueueType.KAFKA.equalsIgnoreCase(annotation.messageQueueType())) {
			return null;
		}

		String topic = annotation.topic();

		String group = null;
		if (StringUtils.isNotBlank(annotation.group())) {
			group = annotation.group();
		} else if (StringUtils.isNotBlank(kafkaProperties.getConsumer().getGroupId())) {
			group = kafkaProperties.getConsumer().getGroupId() + StringConstants.UNDERLINE + topic;
		}

		Consumer<String, String> consumer = consumerFactory.createConsumer(group, kafkaProperties.getClientId());
		consumer.subscribe(Collections.singleton(topic));

		log.debug(CREATE_CONSUMER_FROM_CONSUMER_FACTORY_GROUP_TOPIC, group, topic);
		return consumer;
	}
}
