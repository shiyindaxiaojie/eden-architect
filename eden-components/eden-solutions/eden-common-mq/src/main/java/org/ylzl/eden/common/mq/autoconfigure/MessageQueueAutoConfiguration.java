package org.ylzl.eden.common.mq.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.common.mq.env.MessageQueueProperties;

/**
 * 消息队列自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(name = MessageQueueProperties.ENABLED, havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MessageQueueProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MessageQueueAutoConfiguration {

	public static final String AUTOWIRED_MESSAGE_QUEUE_BEAN_FACTORY = "Autowired MessageQueueBeanFactory";

	private final MessageQueueProperties messageQueueProperties;

	@Bean
	public MessageQueueBeanFactory messageQueueProviderFactory() {
		log.debug(AUTOWIRED_MESSAGE_QUEUE_BEAN_FACTORY);
		return new MessageQueueBeanFactory(messageQueueProperties.getType());
	}
}
