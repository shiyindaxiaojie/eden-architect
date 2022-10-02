package org.ylzl.eden.mail.adapter.core;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ClientErrorType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 短信操作模板实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class SmsTemplateFactory {

	private static final Map<String, String> beanSettings = new ConcurrentHashMap<>();

	private final String defaultType;

	public static void addBean(String type, String beanName) {
		beanSettings.put(type, beanName);
	}


	public SmsTemplate getExecutor() {
		SmsTemplate smsTemplate = StringUtils.isNotBlank(defaultType)?
			ApplicationContextHelper.getBean(beanSettings.get(defaultType.toUpperCase())) :
			ApplicationContextHelper.getBean(SmsTemplate.class);
		ClientErrorType.notNull(smsTemplate, "B0001", "SmsTemplate beanDefinition not found");
		return smsTemplate;
	}

	public SmsTemplate getExecutor(String type) {
		String beanName = beanSettings.get(type.toUpperCase());
		SmsTemplate smsTemplate = ApplicationContextHelper.getBean(beanName, SmsTemplate.class);
		ClientErrorType.notNull(smsTemplate, "B0001", "SmsTemplate beanDefinition named '" + beanName + "' not found");
		return smsTemplate;
	}
}
