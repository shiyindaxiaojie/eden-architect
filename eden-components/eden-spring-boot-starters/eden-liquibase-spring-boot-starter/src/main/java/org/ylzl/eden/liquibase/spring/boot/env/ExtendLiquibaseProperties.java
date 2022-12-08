package org.ylzl.eden.liquibase.spring.boot.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 扩展 LiquibaseProperties
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Setter
@Getter
@ConfigurationProperties(prefix = ExtendLiquibaseProperties.PREFIX)
public class ExtendLiquibaseProperties {

	/** 官方的 LiquibaseProperties 加了 {@code ignoreUnknownFields = false}，很坑 */
	public static final String PREFIX = "liquibase";

	private boolean async;
}
