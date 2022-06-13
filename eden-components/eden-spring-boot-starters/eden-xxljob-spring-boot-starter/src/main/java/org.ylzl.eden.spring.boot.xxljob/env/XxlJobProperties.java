package org.ylzl.eden.spring.boot.xxljob.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * XxlJob 配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 **/
@Data
@ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
public class XxlJobProperties {

	public static final String PREFIX = "xxl-job";

	private final Admin admin = new Admin();

	private final Executor executor = new Executor();

	private boolean enabled;

	@Data
	public static class Admin {

		private String addresses;

		private String accessToken;
	}

	@Data
	public static class Executor {

		private String appName;

		private String ip;

		private int port;

		private String logPath;

		private int logRetentionDays = 30;
	}
}
