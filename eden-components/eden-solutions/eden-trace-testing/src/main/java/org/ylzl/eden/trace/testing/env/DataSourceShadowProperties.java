package org.ylzl.eden.trace.testing.env;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * DataSource 影子库配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Data
@ConfigurationProperties(prefix = DataSourceShadowProperties.PREFIX)
public class DataSourceShadowProperties extends DataSourceProperties {

	public static final String PREFIX = "spring.datasource.shadow";
}
