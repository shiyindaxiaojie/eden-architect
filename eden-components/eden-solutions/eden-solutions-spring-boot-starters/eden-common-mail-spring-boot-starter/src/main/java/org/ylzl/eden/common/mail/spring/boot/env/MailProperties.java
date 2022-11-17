package org.ylzl.eden.common.mail.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.common.mail.spring.boot.autoconfigure.factory.MailBeanType;

/**
 * 邮件配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = MailProperties.PREFIX)
public class MailProperties {

	public static final String PREFIX = "mail";

	public static final String ENABLED = PREFIX + ".enabled";

	private MailBeanType type;
}