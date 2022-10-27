package org.ylzl.eden.common.mail.autoconfigure;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.common.mail.core.MailTemplate;
import org.ylzl.eden.common.mail.core.MailType;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ClientAssert;

import java.util.Objects;

/**
 * 邮件操作模板实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class MailBeanFactory {

	private final MailType defaultType;

	public MailTemplate getExecutor() {
		MailTemplate mailTemplate = ApplicationContextHelper.getBean(defaultType.getTemplateName(), MailTemplate.class);
		ClientAssert.notNull(mailTemplate, "B0001", "MailTemplate beanDefinition not found");
		return mailTemplate;
	}

	public MailTemplate getExecutor(String type) {
		String beanName = Objects.requireNonNull(MailType.parse(type)).getTemplateName();
		MailTemplate mailTemplate = ApplicationContextHelper.getBean(beanName, MailTemplate.class);
		ClientAssert.notNull(mailTemplate, "B0001", "MailTemplate beanDefinition named '" + beanName + "' not found");
		return mailTemplate;
	}
}
