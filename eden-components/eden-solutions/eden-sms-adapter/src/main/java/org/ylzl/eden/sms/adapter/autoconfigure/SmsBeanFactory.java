package org.ylzl.eden.sms.adapter.autoconfigure;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.sms.adapter.core.SmsTemplate;
import org.ylzl.eden.sms.adapter.core.SmsType;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ClientErrorType;

import java.util.Objects;

/**
 * 短信操作实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class SmsBeanFactory {

	private final SmsType defaultType;

	public SmsTemplate getExecutor() {
		SmsTemplate smsTemplate = ApplicationContextHelper.getBean(defaultType.getTemplateName(), SmsTemplate.class);
		ClientErrorType.notNull(smsTemplate, "B0001", "SmsTemplate beanDefinition not found");
		return smsTemplate;
	}

	public SmsTemplate getExecutor(String type) {
		String beanName = Objects.requireNonNull(SmsType.parse(type)).getTemplateName();
		SmsTemplate smsTemplate = ApplicationContextHelper.getBean(beanName, SmsTemplate.class);
		ClientErrorType.notNull(smsTemplate, "B0001", "SmsTemplate beanDefinition named '" + beanName + "' not found");
		return smsTemplate;
	}
}
