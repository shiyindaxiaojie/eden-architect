package org.ylzl.eden.spring.integration.messagequeue.rocketmq.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 完善 RocketMQ 4.7.x 版本的配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Data
@ConfigurationProperties(prefix = "rocketmq.producer")
public class FixedRocketMQProducerProperties {

	/**
	 * Namespace for this MQ Producer instance.
	 */
	private String namespace;
}
