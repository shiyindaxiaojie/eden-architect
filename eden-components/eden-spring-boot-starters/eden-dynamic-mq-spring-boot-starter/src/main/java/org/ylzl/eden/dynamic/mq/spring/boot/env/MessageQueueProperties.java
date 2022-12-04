package org.ylzl.eden.dynamic.mq.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.support.MessageQueueBeanType;

/**
 * 消息队列配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = MessageQueueProperties.PREFIX)
public class MessageQueueProperties {

	public static final String PREFIX = "dynamic-mq";

	public static final String ENABLED = PREFIX + ".enabled";

	private MessageQueueBeanType type;
}
