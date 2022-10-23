package org.ylzl.eden.mq.adapter.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.mq.adapter.core.MessageQueueType;

/**
 * 消息队列配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = MessageQueueProperties.PREFIX)
public class MessageQueueProperties {

	public static final String PREFIX = "message-queue";

	public static final String ENABLED = PREFIX + ".enabled";

	private MessageQueueType type;
}
