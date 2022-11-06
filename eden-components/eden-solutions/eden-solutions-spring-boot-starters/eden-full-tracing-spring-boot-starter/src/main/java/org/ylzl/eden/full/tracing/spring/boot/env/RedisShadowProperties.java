package org.ylzl.eden.full.tracing.spring.boot.env;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 影子库配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Data
@ConfigurationProperties(prefix = RedisShadowProperties.PREFIX)
public class RedisShadowProperties extends RedisProperties {

	public static final String PREFIX = "spring.redis.shadow";
}
