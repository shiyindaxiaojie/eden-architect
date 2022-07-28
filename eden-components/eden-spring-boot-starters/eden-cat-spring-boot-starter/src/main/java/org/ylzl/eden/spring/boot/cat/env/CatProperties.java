package org.ylzl.eden.spring.boot.cat.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "cat")
public class CatProperties {

	public static final String PREFIX = "cat";


}
