package org.ylzl.eden.full.tracing.spring.boot.env;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MongoDB 影子库配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Data
@ConfigurationProperties(prefix = MongoShadowProperties.PREFIX)
public class MongoShadowProperties extends MongoProperties {

	public static final String PREFIX = "spring.data.mongodb.shadow";
}
