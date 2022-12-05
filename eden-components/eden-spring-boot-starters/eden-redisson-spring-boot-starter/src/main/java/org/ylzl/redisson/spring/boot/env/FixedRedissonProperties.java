package org.ylzl.redisson.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redisson 配置补充
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConfigurationProperties(prefix = FixedRedissonProperties.PREFIX)
@Data
public class FixedRedissonProperties {

	public static final String PREFIX = "spring.redis.redisson";

	private int timeout;

	private int connectionPoolSize;

	private int connectionMinimumIdleSize;
}
