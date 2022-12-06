package org.ylzl.eden.dynamic.mail.spring.boot.support;

import lombok.Getter;

/**
 * 邮件注册类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum MailBeanNames {

	JAVAMAIL(MailBeanNames.JAVA_MAIL_TEMPLATE);

	public static final String JAVA_MAIL_TEMPLATE = "javaMailTemplate";

	private final String beanName;

	MailBeanNames(String beanName) {
		this.beanName = beanName;
	}

	public static MailBeanNames parse(String type) {
		for (MailBeanNames mailBeanNames : MailBeanNames.values()) {
			if (mailBeanNames.name().equalsIgnoreCase(type)) {
				return mailBeanNames;
			}
		}
		return null;
	}
}
