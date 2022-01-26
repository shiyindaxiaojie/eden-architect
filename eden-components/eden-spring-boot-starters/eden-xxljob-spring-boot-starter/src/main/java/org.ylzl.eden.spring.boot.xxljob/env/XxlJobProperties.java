package org.ylzl.eden.spring.boot.xxljob.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * XxlJob 配置
 *
 * @author gyl
 * @since 2.4.x
 **/
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

	private final Admin admin = new Admin();

	private final Executor executor = new Executor();

	private Boolean enabled;

	private String accessToken;

	@Data
	public static class Admin {

		private String addresses;
	}

	@Data
	public static class Executor {

		private String appName;

		private String address;

		private String ip;

		private int port;

		private String logPath;

		private int logRetentionDays;
	}
}
