package org.ylzl.eden.common.mq.integration.kafka.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.ylzl.eden.common.mq.autoconfigure.MessageQueueAutoConfiguration;
import org.ylzl.eden.common.mq.core.MessageQueueConsumer;
import org.ylzl.eden.common.mq.core.MessageQueueProvider;
import org.ylzl.eden.common.mq.core.MessageQueueType;
import org.ylzl.eden.common.mq.env.MessageQueueProperties;
import org.ylzl.eden.common.mq.integration.kafka.core.KafkaConsumer;
import org.ylzl.eden.common.mq.integration.kafka.core.KafkaProvider;

import java.util.List;

/**
 * Kafka 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(MessageQueueAutoConfiguration.class)
@ConditionalOnBean(KafkaProperties.class)
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnProperty(value = KafkaMessageQueueAutoConfiguration.ENABLED, matchIfMissing = true)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class KafkaMessageQueueAutoConfiguration {

	public static final String ENABLED = "spring.kafka.enabled";

	public static final String AUTOWIRED_KAKFA_CONSUMER = "Autowired KafkaConsumer";

	public static final String AUTOWIRED_KAFKA_PROVIDER = "Autowired KafkaProvider";

	@Bean(MessageQueueType.KAFKA_CONSUMER)
	public KafkaConsumer kafkaConsumer(MessageQueueProperties messageQueueProperties,
                                       KafkaProperties kafkaProperties,
                                       ObjectProvider<List<MessageQueueConsumer>> messageListeners,
                                       ObjectProvider<ConsumerFactory<String, String>> consumerFactory,
                                       TaskExecutor taskExecutor) {
		log.debug(AUTOWIRED_KAKFA_CONSUMER);
		return new KafkaConsumer(messageQueueProperties, kafkaProperties, messageListeners.getIfAvailable(),
			consumerFactory.getIfAvailable(), taskExecutor);
	}

	@Bean(MessageQueueType.KAFKA_PROVIDER)
	public MessageQueueProvider messageQueueProvider(KafkaTemplate<String, String> kafkaTemplate) {
		log.debug(AUTOWIRED_KAFKA_PROVIDER);
		return new KafkaProvider(kafkaTemplate);
	}
}
