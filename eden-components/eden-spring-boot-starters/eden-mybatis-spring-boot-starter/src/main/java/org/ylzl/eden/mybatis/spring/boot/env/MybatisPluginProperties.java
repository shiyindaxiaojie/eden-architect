package org.ylzl.eden.mybatis.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mybatis 插件配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = MybatisPluginProperties.PREFIX)
public class MybatisPluginProperties {

	public static final String PREFIX = "mybatis.plugin";

	private final SqlLog sqlLog = new SqlLog();

	public static final String SQL_LOG_ENABLED = PREFIX + ".sql-log.enabled";

	@Data
	public static class SqlLog {

		private boolean enabled = true;
	}
}
