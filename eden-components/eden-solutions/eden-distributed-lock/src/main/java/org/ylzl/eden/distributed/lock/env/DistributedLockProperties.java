package org.ylzl.eden.distributed.lock.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 分布式锁配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = DistributedLockProperties.PREFIX)
public class DistributedLockProperties {

	public static final String PREFIX = "distributed-lock";

	private String type;
}
