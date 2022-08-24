package org.ylzl.eden.spring.boot.rocketmq.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.message.queue.core.MessageQueueConsumer;
import org.ylzl.eden.message.queue.core.MessageQueueProvider;
import org.ylzl.eden.message.queue.rocketmq.RocketMQConsumer;
import org.ylzl.eden.message.queue.rocketmq.RocketMQProvider;
import org.ylzl.eden.message.queue.rocketmq.env.FixedRocketMQConsumerProperties;
import org.ylzl.eden.message.queue.rocketmq.env.FixedRocketMQProducerProperties;

import java.util.List;

/**
 * RocketMQ 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(value = "message-queue.rocketmq.enabled", matchIfMissing = true)
@EnableConfigurationProperties({
	FixedRocketMQProducerProperties.class,
	FixedRocketMQConsumerProperties.class
})
@Slf4j
@Configuration(proxyBeanMethods = false)
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
