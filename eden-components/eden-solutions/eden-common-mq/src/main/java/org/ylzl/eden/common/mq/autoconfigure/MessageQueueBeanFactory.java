package org.ylzl.eden.common.mq.autoconfigure;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.common.mq.core.MessageQueueProvider;
import org.ylzl.eden.common.mq.core.MessageQueueType;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ClientAssert;

import java.util.Objects;

/**
 * 消息队列实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class MessageQueueBeanFactory {

	private final MessageQueueType defaultType;

	public MessageQueueProvider getProvider() {
		MessageQueueProvider messageQueueProvider = ApplicationContextHelper
			.getBean(defaultType.getProviderName(), MessageQueueProvider.class);
		ClientAssert.notNull(messageQueueProvider, "SYS-ERROR-500", "MessageQueueProvider beanDefinition not found");
		return messageQueueProvider;
	}

	public MessageQueueProvider getProvider(String type) {
		String beanName = Objects.requireNonNull(MessageQueueType.parse(type)).getProviderName();
		MessageQueueProvider messageQueueProvider = ApplicationContextHelper.getBean(beanName, MessageQueueProvider.class);
		ClientAssert.notNull(messageQueueProvider, "SYS-ERROR-500", "MessageQueueProvider beanDefinition named '" + beanName + "' not found");
		return messageQueueProvider;
	}
}
