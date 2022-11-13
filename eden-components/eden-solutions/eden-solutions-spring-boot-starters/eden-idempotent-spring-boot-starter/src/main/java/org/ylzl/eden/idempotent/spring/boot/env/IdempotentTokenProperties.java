package org.ylzl.eden.idempotent.spring.boot.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * 幂等请求配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
@Setter
@ConfigurationProperties(prefix = IdempotentTokenProperties.PREFIX)
public class IdempotentTokenProperties {

	public static final String PREFIX = "idempotent.token";

	private String prefix = "idempotent:token";

	private long ttl = 10L;

	private TimeUnit timeUnit = TimeUnit.SECONDS;

	private String tokenName = "idempotent";
}
