package org.ylzl.eden.dynamic.mq.spring.boot.support;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.dynamic.mq.core.MessageQueueProvider;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * 消息队列助手
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class MessageQueueHelper {

	private static final String BEAN_DEFINITION_NAMED_NOT_FOUND = "MessageQueueProvider beanDefinition named '{}' not found";

	private final MessageQueueBeanNames primary;

	public MessageQueueProvider getProviderBean() {
		MessageQueueProvider messageQueueProvider = ApplicationContextHelper
			.getBean(primary.getProviderBeanName(), MessageQueueProvider.class);
		AssertUtils.notNull(messageQueueProvider, "SYS-ERROR-500", BEAN_DEFINITION_NAMED_NOT_FOUND,
			primary.getProviderBeanName());
		return messageQueueProvider;
	}

	public MessageQueueProvider getProviderBean(MessageQueueBeanNames type) {
		String beanName = type.getProviderBeanName();
		MessageQueueProvider messageQueueProvider = ApplicationContextHelper.getBean(beanName, MessageQueueProvider.class);
		AssertUtils.notNull(messageQueueProvider, "SYS-ERROR-500", MessageFormatUtils.format(
			BEAN_DEFINITION_NAMED_NOT_FOUND, beanName));
		return messageQueueProvider;
	}
}
