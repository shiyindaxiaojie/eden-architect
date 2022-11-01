package org.ylzl.eden.common.mq.integration.rocketmq.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 完善 RocketMQ 4.7.x 版本的配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "rocketmq.producer")
public class FixedRocketMQProducerProperties {

	/**
	 * Namespace for this MQ Producer instance.
	 */
	private String namespace;
}
