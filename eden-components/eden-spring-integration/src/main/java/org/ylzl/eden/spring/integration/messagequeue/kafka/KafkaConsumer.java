package org.ylzl.eden.spring.integration.messagequeue.kafka;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.ConsumerFactory;
import org.ylzl.eden.spring.integration.messagequeue.annotation.MessageQueueListener;
import org.ylzl.eden.spring.integration.messagequeue.consumer.MessageListener;
import org.ylzl.eden.spring.integration.messagequeue.consumer.MessageQueueConsumer;
import org.ylzl.eden.spring.integration.messagequeue.consumer.MessageQueueConsumerException;

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
public class KafkaConsumer implements MessageQueueConsumer, InitializingBean, DisposableBean {

	private static final String KAFKA_CONSUMER_PROCESSOR_CONSUME_ERROR = "KafkaConsumerProcessor consume error: {}";

	public static final String INITIALIZING_KAFKA_CONSUMER = "Initializing KafkaConsumer";

	public static final String DESTROY_KAFKA_CONSUMER = "Destroy KafkaConsumer";

	private final List<MessageListener> messageListeners;

	private final KafkaProperties kafkaProperties;

	private final ConsumerFactory<String, String> consumerFactory;

	private final TaskExecutor taskExecutor;

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug(INITIALIZING_KAFKA_CONSUMER);
		consume();
	}

	@Override
	public void destroy() throws Exception {
		log.debug(DESTROY_KAFKA_CONSUMER);
	}

	@Override
	public void consume() throws MessageQueueConsumerException {
		for (MessageListener messageListener : messageListeners) {
			taskExecutor.execute(() -> {
				Consumer<String, String> consumer = consumerFactory.createConsumer(
					kafkaProperties.getConsumer().getGroupId(),
					kafkaProperties.getClientId());
				consumer.subscribe(Collections.singleton(topic(messageListener)));
				while (true) {
					try {
						ConsumerRecords<String, String> consumerRecords =
							consumer.poll(kafkaProperties.getConsumer().getFetchMaxWait());
						if (consumerRecords == null || consumerRecords.isEmpty()) {
							continue;
						}
						Map<TopicPartition, OffsetAndMetadata> offsets =
							Maps.newHashMapWithExpectedSize(kafkaProperties.getConsumer().getMaxPollRecords());
						consumerRecords.forEach(record -> {
							offsets.put(new TopicPartition(record.topic(), record.partition()),
								new OffsetAndMetadata(record.offset() + 1));
							// TODO
							messageListener.consume(record.topic(), () -> consumer.commitSync(offsets));
						});
					} catch (Exception e) {
						log.error(KAFKA_CONSUMER_PROCESSOR_CONSUME_ERROR, e.getMessage(), e);
					}
				}
			});
		}
	}

	private static String topic(MessageListener messageListener) {
		Class<? extends MessageListener> clazz = messageListener.getClass();
		MessageQueueListener annotation = clazz.getAnnotation(MessageQueueListener.class);
		return annotation.topic();
	}
}
