package org.ylzl.eden.dynamic.mq.integration.kafka;

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
import org.springframework.kafka.core.ConsumerFactory;
import org.ylzl.eden.dynamic.mq.model.Message;
import org.ylzl.eden.dynamic.mq.MessageQueueConsumer;
import org.ylzl.eden.dynamic.mq.MessageQueueListener;
import org.ylzl.eden.commons.lang.Strings;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * RocketMQ 消费者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer implements InitializingBean, DisposableBean {

	private static final String KAFKA_CONSUMER_PROCESSOR_CONSUME_ERROR = "KafkaConsumerProcessor consume error: {}";

	public static final String INITIALIZING_KAFKA_CONSUMER = "Initializing KafkaConsumer";

	public static final String DESTROY_KAFKA_CONSUMER = "Destroy KafkaConsumer";

	public static final String CREATE_CONSUMER_FROM_CONSUMER_FACTORY_GROUP_TOPIC = "Create consumer from consumerFactory, group: {}, topic: {}";

	private final List<Consumer<String, String>> consumers = Lists.newArrayList();

	private final Function<String, Boolean> matcher;

	private final KafkaProperties kafkaProperties;

	private final List<MessageQueueConsumer> messageQueueConsumers;

	private final ConsumerFactory<String, String> consumerFactory;

	@Override
	public void afterPropertiesSet() {
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
			new Thread(() -> {
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
			}).start();
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
		if (!matcher.apply(annotation.type())) {
			return null;
		}

		String topic = annotation.topic();

		String group = null;
		if (StringUtils.isNotBlank(annotation.group())) {
			group = annotation.group();
		} else if (StringUtils.isNotBlank(kafkaProperties.getConsumer().getGroupId())) {
			group = kafkaProperties.getConsumer().getGroupId() + Strings.UNDERLINE + topic;
		}

		Consumer<String, String> consumer = consumerFactory.createConsumer(group, kafkaProperties.getClientId());
		consumer.subscribe(Collections.singleton(topic));

		log.debug(CREATE_CONSUMER_FROM_CONSUMER_FACTORY_GROUP_TOPIC, group, topic);
		return consumer;
	}


}
