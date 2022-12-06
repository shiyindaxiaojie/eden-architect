package org.ylzl.eden.dynamic.mail.spring.boot.support;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.dynamic.mail.MailTemplate;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * 邮件操作模板助手
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class MailHelper {

	private static final String BEAN_DEFINITION_NAMED_NOT_FOUND = "MailTemplate beanDefinition named '{}' not found";

	private final MailBeanNames primary;

	public MailTemplate getExecutor() {
		MailTemplate mailTemplate = ApplicationContextHelper.getBean(primary.getBeanName(), MailTemplate.class);
		AssertUtils.notNull(mailTemplate, "SYS-ERROR-500", BEAN_DEFINITION_NAMED_NOT_FOUND, primary.getBeanName());
		return mailTemplate;
	}

	public MailTemplate getExecutor(MailBeanNames mailBeanNames) {
		String beanName = mailBeanNames.getBeanName();
		MailTemplate mailTemplate = ApplicationContextHelper.getBean(beanName, MailTemplate.class);
		AssertUtils.notNull(mailTemplate, "SYS-ERROR-500",
			MessageFormatUtils.format(BEAN_DEFINITION_NAMED_NOT_FOUND, beanName));
		return mailTemplate;
	}
}
