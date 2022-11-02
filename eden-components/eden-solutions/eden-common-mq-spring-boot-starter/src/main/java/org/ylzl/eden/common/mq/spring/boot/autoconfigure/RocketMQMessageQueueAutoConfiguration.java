package org.ylzl.eden.common.mq.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.common.mq.autoconfigure.MessageQueueAutoConfiguration;
import org.ylzl.eden.common.mq.MessageQueueConsumer;
import org.ylzl.eden.common.mq.MessageQueueProvider;
import org.ylzl.eden.common.mq.autoconfigure.MessageQueueBeanType;
import org.ylzl.eden.common.mq.env.MessageQueueProperties;
import org.ylzl.eden.common.mq.integration.rocketmq.RocketMQProvider;
import org.ylzl.eden.common.mq.integration.rocketmq.env.FixedRocketMQConsumerProperties;
import org.ylzl.eden.common.mq.integration.rocketmq.env.FixedRocketMQProducerProperties;
import org.ylzl.eden.common.mq.integration.rocketmq.RocketMQConsumer;
import org.ylzl.eden.commons.lang.StringUtils;

import java.util.List;
import java.util.function.Function;

/**
 * RocketMQ 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(MessageQueueAutoConfiguration.class)
@ConditionalOnBean(RocketMQProperties.class)
@ConditionalOnClass(RocketMQTemplate.class)
@ConditionalOnProperty(value = RocketMQMessageQueueAutoConfiguration.ENABLED, matchIfMissing = true)
@EnableConfigurationProperties({
	FixedRocketMQProducerProperties.class,
	FixedRocketMQConsumerProperties.class
})
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RocketMQMessageQueueAutoConfiguration {

	public static final String ENABLED = "rocketmq.enabled";

	public static final String AUTOWIRED_ROCKET_MQ_CONSUMER = "Autowired RocketMQConsumer";

	public static final String AUTOWIRED_ROCKET_MQ_PROVIDER = "Autowired RocketMQProvider";

	@Bean(MessageQueueBeanType.ROCKETMQ_CONSUMER)
	public RocketMQConsumer rocketMQConsumer(MessageQueueProperties messageQueueProperties,
                                             RocketMQProperties rocketMQProperties,
                                             FixedRocketMQConsumerProperties fixedRocketMQConsumerProperties,
                                             ObjectProvider<List<MessageQueueConsumer>> messageListeners) {
		log.debug(AUTOWIRED_ROCKET_MQ_CONSUMER);
		Function<String, Boolean> matcher = type -> StringUtils.isBlank(type)?
			MessageQueueBeanType.ROCKETMQ.name().equalsIgnoreCase(messageQueueProperties.getType().name()):
			MessageQueueBeanType.ROCKETMQ.name().equalsIgnoreCase(type);
		return new RocketMQConsumer(matcher, rocketMQProperties, fixedRocketMQConsumerProperties,
			messageListeners.getIfAvailable());
	}

	@Bean(MessageQueueBeanType.ROCKETMQ_PROVIDER)
	public MessageQueueProvider messageQueueProvider(RocketMQTemplate rocketMQTemplate,
                                                     FixedRocketMQProducerProperties fixedRocketMQProducerProperties) {
		log.debug(AUTOWIRED_ROCKET_MQ_PROVIDER);
		return new RocketMQProvider(rocketMQTemplate, fixedRocketMQProducerProperties);
	}
}
