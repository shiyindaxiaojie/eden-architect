package org.ylzl.eden.distributed.uid.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 分布式唯一ID配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = DistributedUIDProperties.PREFIX)
public class DistributedUIDProperties {

	public static final String PREFIX = "distributed-uid";

	private String type;
}
