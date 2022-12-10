package org.ylzl.eden.dynamic.mq.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.dynamic.mq.MessageQueueConsumer;
import org.ylzl.eden.dynamic.mq.MessageQueueProvider;
import org.ylzl.eden.dynamic.mq.integration.rocketmq.RocketMQConsumer;
import org.ylzl.eden.dynamic.mq.integration.rocketmq.RocketMQProvider;
import org.ylzl.eden.dynamic.mq.integration.rocketmq.config.RocketMQConfig;
import org.ylzl.eden.dynamic.mq.spring.boot.env.MessageQueueProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.env.RocketMQConsumerProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.env.RocketMQProducerProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.env.convertor.RocketMQConvertor;
import org.ylzl.eden.dynamic.mq.spring.boot.support.MessageQueueBeanNames;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import java.util.List;
import java.util.function.Function;

/**
 * RocketMQ 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = MessageQueueProperties.RocketMQ.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnExpression("${rocketmq.enabled:true}")
@ConditionalOnBean(RocketMQProperties.class)
@ConditionalOnClass(RocketMQTemplate.class)
@AutoConfigureAfter(MessageQueueAutoConfiguration.class)
@EnableConfigurationProperties({
	RocketMQProducerProperties.class,
	RocketMQConsumerProperties.class
})
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RocketMQMessageQueueAutoConfiguration {

	private static final String AUTOWIRED_ROCKET_MQ_CONSUMER = "Autowired RocketMQConsumer";

	private static final String AUTOWIRED_ROCKET_MQ_PROVIDER = "Autowired RocketMQProvider";

	private final MessageQueueProperties messageQueueProperties;

	private final RocketMQProperties rocketMQProperties;

	@Bean(MessageQueueBeanNames.ROCKETMQ_CONSUMER)
	public RocketMQConsumer rocketMQConsumer(RocketMQConsumerProperties rocketMQConsumerProperties,
											 ObjectProvider<List<MessageQueueConsumer>> messageListeners) {
		log.debug(AUTOWIRED_ROCKET_MQ_CONSUMER);
		Function<String, Boolean> matcher = type -> StringUtils.isBlank(type) && messageQueueProperties.getPrimary() != null?
			MessageQueueBeanNames.ROCKETMQ.name().equalsIgnoreCase(messageQueueProperties.getPrimary().name()):
			MessageQueueBeanNames.ROCKETMQ.name().equalsIgnoreCase(type);
		RocketMQConfig config = RocketMQConvertor.INSTANCE.toConfig(rocketMQProperties);
		RocketMQConvertor.INSTANCE.updateConfigFromConsumer(rocketMQConsumerProperties, config.getConsumer());
		return new RocketMQConsumer(config, messageListeners.getIfAvailable(), matcher);
	}

	@Bean(MessageQueueBeanNames.ROCKETMQ_PROVIDER)
	public MessageQueueProvider messageQueueProvider(RocketMQProducerProperties rocketMQProducerProperties,
													 RocketMQTemplate rocketMQTemplate) {
		log.debug(AUTOWIRED_ROCKET_MQ_PROVIDER);
		RocketMQConfig config = RocketMQConvertor.INSTANCE.toConfig(rocketMQProperties);
		RocketMQConvertor.INSTANCE.updateConfigFromProducer(rocketMQProducerProperties, config.getProducer());
		return new RocketMQProvider(config, rocketMQTemplate);
	}
}
