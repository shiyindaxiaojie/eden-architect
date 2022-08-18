package org.ylzl.eden.spring.boot.mybatis.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = "mybatis-plus.extension")
public class MybatisPlusExtensionProperties {

	private boolean sqlLog = true;

	private boolean autoFill = true;
}
