package org.ylzl.eden.mail.adapter.core;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ClientErrorType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮件操作模板实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class MailTemplateFactory {

	private static final Map<String, String> beanSettings = new ConcurrentHashMap<>();

	private final String defaultType;

	public static void addBean(String type, String beanName) {
		beanSettings.put(type, beanName);
	}


	public MailTemplate getExecutor() {
		MailTemplate mailTemplate = StringUtils.isNotBlank(defaultType)?
			ApplicationContextHelper.getBean(beanSettings.get(defaultType.toUpperCase())) :
			ApplicationContextHelper.getBean(MailTemplate.class);
		ClientErrorType.notNull(mailTemplate, "B0001", "MailTemplate beanDefinition not found");
		return mailTemplate;
	}

	public MailTemplate getExecutor(String type) {
		String beanName = beanSettings.get(type.toUpperCase());
		MailTemplate mailTemplate = ApplicationContextHelper.getBean(beanName, MailTemplate.class);
		ClientErrorType.notNull(mailTemplate, "B0001", "MailTemplate beanDefinition named '" + beanName + "' not found");
		return mailTemplate;
	}
}
