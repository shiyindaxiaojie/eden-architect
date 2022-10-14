package org.ylzl.eden.mq.adapter.rocketmq.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.mq.adapter.autoconfigure.MessageQueueAutoConfiguration;
import org.ylzl.eden.mq.adapter.core.MessageQueueConsumer;
import org.ylzl.eden.mq.adapter.core.MessageQueueProvider;
import org.ylzl.eden.mq.adapter.core.MessageQueueProviderFactory;
import org.ylzl.eden.mq.adapter.env.MessageQueueProperties;
import org.ylzl.eden.mq.adapter.rocketmq.core.RocketMQConsumer;
import org.ylzl.eden.mq.adapter.rocketmq.core.RocketMQProvider;
import org.ylzl.eden.mq.adapter.rocketmq.env.FixedRocketMQConsumerProperties;
import org.ylzl.eden.mq.adapter.rocketmq.env.FixedRocketMQProducerProperties;

import java.util.List;

/**
 * RocketMQ 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(MessageQueueAutoConfiguration.class)
@ConditionalOnClass(RocketMQTemplate.class)
@ConditionalOnProperty(value = RocketMQMessageQueueAutoConfiguration.ENABLED, matchIfMissing = true)
@EnableConfigurationProperties({
	FixedRocketMQProducerProperties.class,
	FixedRocketMQConsumerProperties.class
})
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RocketMQMessageQueueAutoConfiguration {

	public static final String ENABLED = "message-queue.rocketmq.enabled";

	public static final String TYPE = "ROCKETMQ";

	public static final String BEAN_CONSUMER = "rocketMQConsumer";

	public static final String BEAN_PROVIDER = "rocketMQProvider";

	public static final String AUTOWIRED_ROCKET_MQ_CONSUMER = "Autowired RocketMQConsumer";

	public static final String AUTOWIRED_ROCKET_MQ_PROVIDER = "Autowired RocketMQProvider";

	@Bean(BEAN_CONSUMER)
	public RocketMQConsumer rocketMQConsumer(MessageQueueProperties messageQueueProperties,
											 RocketMQProperties rocketMQProperties,
											 FixedRocketMQConsumerProperties fixedRocketMQConsumerProperties,
											 ObjectProvider<List<MessageQueueConsumer>> messageListeners) {
		log.debug(AUTOWIRED_ROCKET_MQ_CONSUMER);
		return new RocketMQConsumer(messageQueueProperties, rocketMQProperties, fixedRocketMQConsumerProperties,
			messageListeners.getIfAvailable());
	}

	@Bean(BEAN_PROVIDER)
	public MessageQueueProvider messageQueueProvider(RocketMQTemplate rocketMQTemplate,
													 FixedRocketMQProducerProperties fixedRocketMQProducerProperties) {
		log.debug(AUTOWIRED_ROCKET_MQ_PROVIDER);
		MessageQueueProviderFactory.addBean(TYPE, BEAN_PROVIDER);
		return new RocketMQProvider(rocketMQTemplate, fixedRocketMQProducerProperties);
	}
}
