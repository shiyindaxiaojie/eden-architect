package org.ylzl.eden.full.link.stress.testing.redis.env;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 影子库配置
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Data
@ConfigurationProperties(prefix = RedisShadowProperties.PREFIX)
public class RedisShadowProperties extends RedisProperties {

	public static final String PREFIX = "spring.redis.shadow";
}
