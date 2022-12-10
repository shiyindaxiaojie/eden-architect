package org.ylzl.eden.dynamic.mq.spring.boot.env;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.dynamic.mq.spring.boot.support.MessageQueueBeanNames;

/**
 * 消息队列配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = MessageQueueProperties.PREFIX)
public class MessageQueueProperties {

	public static final String PREFIX = "spring.message-queue.dynamic";

	private Boolean enabled;

	private MessageQueueBeanNames primary;

	private final RocketMQ rocketMQ = new RocketMQ();

	private final Kafka kafka = new Kafka();

	@Setter
	@Getter
	public static class RocketMQ {

		public static final String PREFIX = MessageQueueProperties.PREFIX + ".rocketmq";

		private boolean enabled;
	}

	@Setter
	@Getter
	public static class Kafka {

		public static final String PREFIX = MessageQueueProperties.PREFIX + ".kafka";

		private boolean enabled;
	}
}
