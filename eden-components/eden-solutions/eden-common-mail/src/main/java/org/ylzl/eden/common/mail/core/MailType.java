package org.ylzl.eden.common.mail.core;

import lombok.Getter;

/**
 * 邮件类型
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Getter
public enum MailType {

	DEFAULT(null),
	JAVA_MAIL(MailType.JAVA_MAIL_TEMPLATE);

	public static final String JAVA_MAIL_TEMPLATE = "javaMailTemplate";

	private final String templateName;

	MailType(String templateName) {
		this.templateName = templateName;
	}

	public static MailType parse(String type) {
		for (MailType mailType : MailType.values()) {
			if (mailType.name().equalsIgnoreCase(type)) {
				return mailType;
			}
		}
		return null;
	}
}
