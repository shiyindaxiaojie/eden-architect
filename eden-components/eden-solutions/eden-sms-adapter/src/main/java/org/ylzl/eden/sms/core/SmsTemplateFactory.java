package org.ylzl.eden.sms.core;

import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ClientErrorType;

/**
 * 短信操作模板工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class SmsTemplateFactory {

	public SmsTemplate getExecutor() {
		SmsTemplate smsTemplate = ApplicationContextHelper.getBean(SmsTemplate.class);
		ClientErrorType.notNull(smsTemplate, "B0001", "SmsTemplate beanDefinition not found");
		return smsTemplate;
	}

	public SmsTemplate getExecutor(String beanName) {
		SmsTemplate smsTemplate = ApplicationContextHelper.getBean(beanName, SmsTemplate.class);
		ClientErrorType.notNull(smsTemplate, "B0001", "SmsTemplate beanDefinition named '" + beanName + "' not found");
		return smsTemplate;
	}
}
