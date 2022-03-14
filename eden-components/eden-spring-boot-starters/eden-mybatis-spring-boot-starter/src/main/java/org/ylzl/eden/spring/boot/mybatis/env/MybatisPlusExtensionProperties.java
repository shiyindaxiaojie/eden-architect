package org.ylzl.eden.spring.boot.mybatis.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Data
@ConfigurationProperties(prefix = "mybatis-plus.extension")
public class MybatisPlusExtensionProperties {

	private Boolean sqlLog = true;

	private Boolean autoFill = true;
}
