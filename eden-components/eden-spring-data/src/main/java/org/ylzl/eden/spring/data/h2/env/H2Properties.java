package org.ylzl.eden.spring.data.h2.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.spring.framework.core.constant.SpringFrameworkConstants;

/**
 * H2 配置属性
 *
 * @author gyl
 * @since 2.4.x
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SpringFrameworkConstants.PROP_PREFIX + ".h2")
public class H2Properties {

	private Boolean enabled = H2Defaults.enabled;

	private Integer port;
}
