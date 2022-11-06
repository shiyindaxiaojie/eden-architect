package org.ylzl.eden.common.mq.integration.rocketmq.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 完善 RocketMQ 4.7.x 版本的配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
@Setter
public class RocketMQProducerConfig {

	/**
	 * Namespace for this MQ Producer instance.
	 */
	private String namespace;
}
