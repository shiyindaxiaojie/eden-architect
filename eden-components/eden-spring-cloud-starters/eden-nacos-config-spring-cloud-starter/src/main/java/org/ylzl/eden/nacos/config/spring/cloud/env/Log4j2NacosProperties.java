package org.ylzl.eden.nacos.config.spring.cloud.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Log4j2 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Setter
@Getter
@ConfigurationProperties(prefix = Log4j2NacosProperties.PREFIX)
public class Log4j2NacosProperties {

	public static final String PREFIX = "log4j2";

	private boolean enabled = false;

	private final Nacos nacos = new Nacos();

	@Setter
	@Getter
	public static class Nacos {

		private String group;

		private String dataId = "log4j2.yml";
	}
}
