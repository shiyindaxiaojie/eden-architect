package org.ylzl.eden.spring.boot.distributelock.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.integration.core.constant.SpringIntegrationConstants;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Data
@ConfigurationProperties(prefix = SpringIntegrationConstants.PROP_PREFIX + ".distribute-lock")
public class DistributedLockProperties {

	private DistributedLockEnum distributedLockEnum;
}
