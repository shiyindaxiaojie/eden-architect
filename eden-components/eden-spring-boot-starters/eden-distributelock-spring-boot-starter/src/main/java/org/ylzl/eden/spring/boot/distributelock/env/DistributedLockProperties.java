package org.ylzl.eden.spring.boot.distributelock.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.framework.core.constant.GlobalConstants;

/**
 * 分布式锁配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Data
@ConfigurationProperties(prefix = GlobalConstants.PROP_EDEN_PREFIX + ".distribute-lock")
public class DistributedLockProperties {

	private String type;
}
