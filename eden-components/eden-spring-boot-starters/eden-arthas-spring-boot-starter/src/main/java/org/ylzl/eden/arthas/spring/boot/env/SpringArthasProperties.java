package org.ylzl.eden.arthas.spring.boot.env;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Arthas 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Data
@ConfigurationProperties(prefix = SpringArthasProperties.PREFIX)
public class SpringArthasProperties {

	public static final String PREFIX = "spring.arthas";

	public static final String ENABLED = "spring.arthas.enabled";

	private boolean enable = false;
}
