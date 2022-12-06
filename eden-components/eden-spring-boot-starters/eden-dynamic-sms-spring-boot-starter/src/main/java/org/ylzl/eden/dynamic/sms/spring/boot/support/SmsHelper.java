package org.ylzl.eden.dynamic.sms.spring.boot.support;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.dynamic.sms.core.SmsTemplate;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * 短信操作助手
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class SmsHelper {

	private static final String BEAN_DEFINITION_NOT_FOUND = "SmsTemplate beanDefinition named '{}' not found";

	private final SmsBeanNames primary;

	public SmsTemplate getBean() {
		SmsTemplate smsTemplate = ApplicationContextHelper.getBean(primary.getBeanName(), SmsTemplate.class);
		AssertUtils.notNull(smsTemplate, "SYS-ERROR-500", BEAN_DEFINITION_NOT_FOUND, primary.getBeanName());
		return smsTemplate;
	}

	public SmsTemplate getBean(SmsBeanNames smsBeanNames) {
		String beanName = smsBeanNames.getBeanName();
		SmsTemplate smsTemplate = ApplicationContextHelper.getBean(beanName, SmsTemplate.class);
		AssertUtils.notNull(smsTemplate, "SYS-ERROR-500",
			MessageFormatUtils.format(BEAN_DEFINITION_NOT_FOUND, beanName, smsBeanNames.name()));
		return smsTemplate;
	}
}
