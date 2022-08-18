package org.ylzl.eden.spring.boot.kafka.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.ylzl.eden.spring.integration.messagequeue.core.MessageQueueConsumer;
import org.ylzl.eden.spring.integration.messagequeue.core.MessageQueueProvider;
import org.ylzl.eden.spring.integration.messagequeue.kafka.KafkaConsumer;
import org.ylzl.eden.spring.integration.messagequeue.kafka.KafkaProvider;

import java.util.List;

/**
 * Kafka 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(value = "message-queue.kafka.enabled", matchIfMissing = true)
@Slf4j
@Configuration
public class KafkaMessageQueueAutoConfiguration {

	@Bean
	public KafkaConsumer kafkaConsumer(KafkaProperties kafkaProperties,
									   ObjectProvider<List<MessageQueueConsumer>> messageListeners,
									   ObjectProvider<ConsumerFactory<String, String>> consumerFactory,
									   TaskExecutor taskExecutor) {
		return new KafkaConsumer(kafkaProperties, messageListeners.getIfAvailable(),
			consumerFactory.getIfAvailable(), taskExecutor);
	}

	@Bean("kafkaProvider")
	public MessageQueueProvider messageQueueProvider(KafkaTemplate<String, String> kafkaTemplate) {
		return new KafkaProvider(kafkaTemplate);
	}
}
