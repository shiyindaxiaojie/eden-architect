package org.ylzl.eden.idempotent.spring.boot.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 幂等请求配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Getter
@Setter
@ConfigurationProperties(prefix = IdempotentTtlProperties.PREFIX)
public class IdempotentTtlProperties {

	public static final String PREFIX = "idempotent.ttl";

	private String prefix = "idempotent:ttl";
}
