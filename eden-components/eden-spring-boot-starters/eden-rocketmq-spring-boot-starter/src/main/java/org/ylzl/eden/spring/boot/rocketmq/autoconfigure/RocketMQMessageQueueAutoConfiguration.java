package org.ylzl.eden.spring.boot.rocketmq.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.integration.messagequeue.core.MessageQueueConsumer;
import org.ylzl.eden.spring.integration.messagequeue.core.MessageQueueProvider;
import org.ylzl.eden.spring.integration.messagequeue.rocketmq.FixedRocketMQConsumerProperties;
import org.ylzl.eden.spring.integration.messagequeue.rocketmq.FixedRocketMQProducerProperties;
import org.ylzl.eden.spring.integration.messagequeue.rocketmq.RocketMQConsumer;
import org.ylzl.eden.spring.integration.messagequeue.rocketmq.RocketMQProvider;

import java.util.List;

/**
 * RocketMQ 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@ConditionalOnProperty(value = "message-queue.rocketmq.enabled", matchIfMissing = true)
@EnableConfigurationProperties({
	FixedRocketMQProducerProperties.class,
	FixedRocketMQConsumerProperties.class
})
@Slf4j
@Configuration
public class RocketMQMessageQueueAutoConfiguration {

	@Bean
	public RocketMQConsumer rocketMQConsumer(RocketMQProperties rocketMQProperties,
											 FixedRocketMQConsumerProperties fixedRocketMQConsumerProperties,
											 ObjectProvider<List<MessageQueueConsumer>> messageListeners) {
		return new RocketMQConsumer(rocketMQProperties, fixedRocketMQConsumerProperties,
			messageListeners.getIfAvailable());
	}

	@Bean("rocketMQProvider")
	public MessageQueueProvider messageQueueProvider(RocketMQTemplate rocketMQTemplate,
													 FixedRocketMQProducerProperties fixedRocketMQProducerProperties) {
		return new RocketMQProvider(rocketMQTemplate, fixedRocketMQProducerProperties);
	}
}
