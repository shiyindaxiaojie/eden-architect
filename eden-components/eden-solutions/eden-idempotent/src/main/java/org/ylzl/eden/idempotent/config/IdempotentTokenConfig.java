package org.ylzl.eden.idempotent.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 * 幂等请求配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@Getter
@Setter
public class IdempotentTokenConfig {

	private String prefix = "idempotent:token";

	private long ttl = 10L;

	private TimeUnit timeUnit = TimeUnit.SECONDS;
}
