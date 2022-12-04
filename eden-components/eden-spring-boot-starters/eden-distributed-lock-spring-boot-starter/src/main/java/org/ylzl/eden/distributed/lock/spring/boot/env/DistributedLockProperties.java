package org.ylzl.eden.distributed.lock.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.distributed.lock.spring.boot.support.DistributedLockBeanType;

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

	private DistributedLockBeanType primary;
}
