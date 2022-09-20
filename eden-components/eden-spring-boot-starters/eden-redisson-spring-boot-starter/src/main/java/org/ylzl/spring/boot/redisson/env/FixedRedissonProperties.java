package org.ylzl.spring.boot.redisson.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redisson 配置补充
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConfigurationProperties(prefix = "spring.redis.redisson")
@Data
public class FixedRedissonProperties {

	private int timeout;

	private int connectionPoolSize;

	private int connectionMinimumIdleSize;
}
