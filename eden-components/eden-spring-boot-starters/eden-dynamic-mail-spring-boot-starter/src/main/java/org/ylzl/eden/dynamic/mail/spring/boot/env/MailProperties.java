package org.ylzl.eden.dynamic.mail.spring.boot.env;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ylzl.eden.dynamic.mail.spring.boot.support.MailBeanNames;

/**
 * 邮件配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Data
@ConfigurationProperties(prefix = MailProperties.PREFIX)
public class MailProperties {

	public static final String PREFIX = "spring.mail.dynamic";

	private Boolean enabled;

	private MailBeanNames primary;

	private final JavaMail javaMail = new JavaMail();

	@Setter
	@Getter
	public static class JavaMail {

		public static final String PREFIX = MailProperties.PREFIX + ".javamail";

		private boolean enabled;
	}
}
