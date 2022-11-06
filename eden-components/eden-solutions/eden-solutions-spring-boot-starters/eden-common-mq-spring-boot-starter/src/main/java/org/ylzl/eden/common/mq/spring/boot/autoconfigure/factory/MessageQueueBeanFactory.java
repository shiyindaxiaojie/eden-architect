package org.ylzl.eden.common.mq.spring.boot.autoconfigure.factory;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.common.mq.core.MessageQueueProvider;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.util.AssertUtils;
import org.ylzl.eden.commons.lang.MessageFormatUtils;

/**
 * 消息队列实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class MessageQueueBeanFactory {

	private static final String BEAN_DEFINITION_NOT_FOUND = "MessageQueueProvider beanDefinition not found";

	private static final String BEAN_DEFINITION_NAMED_NOT_FOUND = "MessageQueueProvider beanDefinition named '{}' not found";

	private final MessageQueueBeanType defaultType;

	public MessageQueueProvider getProvider() {
		MessageQueueProvider messageQueueProvider = ApplicationContextHelper
			.getBean(defaultType.getProviderName(), MessageQueueProvider.class);
		AssertUtils.notNull(messageQueueProvider, "SYS-ERROR-500", BEAN_DEFINITION_NOT_FOUND);
		return messageQueueProvider;
	}

	public MessageQueueProvider getProvider(MessageQueueBeanType type) {
		String beanName = type.getProviderName();
		MessageQueueProvider messageQueueProvider = ApplicationContextHelper.getBean(beanName, MessageQueueProvider.class);
		AssertUtils.notNull(messageQueueProvider, "SYS-ERROR-500", MessageFormatUtils.format(
			BEAN_DEFINITION_NAMED_NOT_FOUND, beanName));
		return messageQueueProvider;
	}
}
