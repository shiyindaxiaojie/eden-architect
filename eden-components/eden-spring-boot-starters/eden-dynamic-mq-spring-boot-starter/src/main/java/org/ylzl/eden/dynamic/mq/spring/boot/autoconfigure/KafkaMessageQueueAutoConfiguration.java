package org.ylzl.eden.dynamic.mq.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.ylzl.eden.dynamic.mq.core.MessageQueueConsumer;
import org.ylzl.eden.dynamic.mq.core.MessageQueueProvider;
import org.ylzl.eden.dynamic.mq.integration.kafka.KafkaConsumer;
import org.ylzl.eden.dynamic.mq.integration.kafka.KafkaProvider;
import org.ylzl.eden.dynamic.mq.spring.boot.support.MessageQueueBeanNames;
import org.ylzl.eden.dynamic.mq.spring.boot.env.MessageQueueProperties;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import java.util.List;
import java.util.function.Function;

/**
 * Kafka 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = MessageQueueProperties.Kafka.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.ENABLED_TRUE
)
@ConditionalOnExpression("${spring.kafka.enabled:true}")
@ConditionalOnBean(KafkaProperties.class)
@ConditionalOnClass(KafkaTemplate.class)
@AutoConfigureAfter(MessageQueueAutoConfiguration.class)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class KafkaMessageQueueAutoConfiguration {

	private static final String AUTOWIRED_KAKFA_CONSUMER = "Autowired KafkaConsumer";

	private static final String AUTOWIRED_KAFKA_PROVIDER = "Autowired KafkaProvider";

	@Bean(MessageQueueBeanNames.KAFKA_CONSUMER)
	public KafkaConsumer kafkaConsumer(MessageQueueProperties messageQueueProperties,
									   KafkaProperties kafkaProperties,
									   ObjectProvider<List<MessageQueueConsumer>> messageListeners,
									   ObjectProvider<ConsumerFactory<String, String>> consumerFactory) {
		log.debug(AUTOWIRED_KAKFA_CONSUMER);
		Function<String, Boolean> matcher = type -> StringUtils.isBlank(type)?
			MessageQueueBeanNames.KAFKA.name().equalsIgnoreCase(messageQueueProperties.getPrimary().name()):
			MessageQueueBeanNames.KAFKA.name().equalsIgnoreCase(type);
		return new KafkaConsumer(matcher,
			kafkaProperties, messageListeners.getIfAvailable(),
			consumerFactory.getIfAvailable());
	}

	@Bean(MessageQueueBeanNames.KAFKA_PROVIDER)
	public MessageQueueProvider messageQueueProvider(KafkaTemplate<String, String> kafkaTemplate) {
		log.debug(AUTOWIRED_KAFKA_PROVIDER);
		return new KafkaProvider(kafkaTemplate);
	}
}
