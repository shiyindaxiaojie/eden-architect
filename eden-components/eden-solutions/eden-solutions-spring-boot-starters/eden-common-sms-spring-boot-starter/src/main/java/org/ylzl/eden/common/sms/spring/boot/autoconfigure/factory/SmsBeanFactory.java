package org.ylzl.eden.common.sms.spring.boot.autoconfigure.factory;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.common.sms.core.SmsTemplate;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * 短信操作实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class SmsBeanFactory {

	private static final String BEAN_DEFINITION_NOT_FOUND = "SmsTemplate beanDefinition not found";

	private static final String BEAN_DEFINITION_NOT_FOUND_BY = "SmsTemplate beanDefinition named '{}' not found by '{}'";

	private final SmsBeanType defaultType;

	public SmsTemplate getExecutor() {
		SmsTemplate smsTemplate = ApplicationContextHelper.getBean(defaultType.getTemplateName(), SmsTemplate.class);
		AssertUtils.notNull(smsTemplate, "SYS-ERROR-500", BEAN_DEFINITION_NOT_FOUND);
		return smsTemplate;
	}

	public SmsTemplate getExecutor(SmsBeanType smsBeanType) {
		String beanName = smsBeanType.getTemplateName();
		SmsTemplate smsTemplate = ApplicationContextHelper.getBean(beanName, SmsTemplate.class);
		AssertUtils.notNull(smsTemplate, "SYS-ERROR-500",
			MessageFormatUtils.format(BEAN_DEFINITION_NOT_FOUND_BY, beanName, smsBeanType.name()));
		return smsTemplate;
	}
}
