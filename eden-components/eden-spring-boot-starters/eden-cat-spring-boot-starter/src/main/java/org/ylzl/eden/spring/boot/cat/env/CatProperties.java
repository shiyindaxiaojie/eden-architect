package org.ylzl.eden.spring.boot.cat.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cat 属性配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "cat")
public class CatProperties {

	public static final String PREFIX = "cat";

	private boolean enabled;

	private String home = "/tmp";

	private String domain;

	private String servers;

	private int tcpPort = 2280;

	private int httpPort = 80;
}
