package org.ylzl.eden.common.mail.spring.boot.autoconfigure.factory;

import lombok.Getter;

/**
 * 邮件注册类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum MailBeanType {

	DEFAULT(null),
	JAVA_MAIL(MailBeanType.JAVA_MAIL_TEMPLATE);

	public static final String JAVA_MAIL_TEMPLATE = "javaMailTemplate";

	private final String templateName;

	MailBeanType(String templateName) {
		this.templateName = templateName;
	}

	public static MailBeanType parse(String type) {
		for (MailBeanType mailBeanType : MailBeanType.values()) {
			if (mailBeanType.name().equalsIgnoreCase(type)) {
				return mailBeanType;
			}
		}
		return null;
	}
}
