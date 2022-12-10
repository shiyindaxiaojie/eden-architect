package org.ylzl.eden.dynamic.mq.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.dynamic.mq.spring.boot.env.MessageQueueProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.support.MessageQueueHelper;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * 消息队列自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = MessageQueueProperties.PREFIX,
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@EnableConfigurationProperties(MessageQueueProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MessageQueueAutoConfiguration {

	public static final String AUTOWIRED_MESSAGE_QUEUE_BEAN_FACTORY = "Autowired MessageQueueBeanFactory";

	private final MessageQueueProperties messageQueueProperties;

	@Bean
	public MessageQueueHelper messageQueueHelper() {
		log.debug(AUTOWIRED_MESSAGE_QUEUE_BEAN_FACTORY);
		return new MessageQueueHelper(messageQueueProperties.getPrimary());
	}
}
