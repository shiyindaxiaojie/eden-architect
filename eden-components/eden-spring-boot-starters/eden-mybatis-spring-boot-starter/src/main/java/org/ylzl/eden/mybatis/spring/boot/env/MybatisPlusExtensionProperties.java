package org.ylzl.eden.mybatis.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MybatisPlus 扩展配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = MybatisPlusExtensionProperties.PREFIX)
public class MybatisPlusExtensionProperties {

	public static final String PREFIX = "mybatis-plus.extension";

	public static final String AUTO_FILL_ENABLED = PREFIX + ".auto-fill.enabled";

	private final AutoFill autoFill = new AutoFill();

	@Data
	public static class AutoFill {

		private boolean enabled = true;

		private String createDateFieldName = "create_date";

		private String lastModifiedDateFieldName = "last_modified_date";
	}
}
