package org.ylzl.eden.spring.integration.sms.core;

import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ClientErrorType;

/**
 * 短信操作模板策略
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class SmsTemplateStrategy {

	public SmsTemplate getExecutor(String beanName) {
		SmsTemplate smsTemplate = ApplicationContextHelper.getBean(beanName, SmsTemplate.class);
		ClientErrorType.notNull(smsTemplate,"B0001", "SmsTemplate beanDefinition named '" + beanName + "' not found");
		return smsTemplate;
	}
}
