package org.ylzl.eden.mq.adapter.core;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;
import org.ylzl.eden.spring.framework.error.ClientErrorType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息队列生产者实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class MessageQueueProviderFactory {

	private static final Map<String, String> beanSettings = new ConcurrentHashMap<>();

	private final String defaultType;

	public static void addBean(String type, String beanName) {
		beanSettings.put(type, beanName);
	}

	public MessageQueueProvider getProvider() {
		MessageQueueProvider messageQueueProvider = StringUtils.isNotBlank(defaultType)?
			ApplicationContextHelper.getBean(beanSettings.get(defaultType.toUpperCase())) :
			ApplicationContextHelper.getBean(MessageQueueProvider.class);
		ClientErrorType.notNull(messageQueueProvider, "B0001", "MessageQueueProvider beanDefinition not found");
		return messageQueueProvider;
	}

	public MessageQueueProvider getProvider(String type) {
		String beanName = beanSettings.get(type.toUpperCase());
		MessageQueueProvider messageQueueProvider = ApplicationContextHelper.getBean(beanName, MessageQueueProvider.class);
		ClientErrorType.notNull(messageQueueProvider, "B0001", "MessageQueueProvider beanDefinition named '" + beanName + "' not found");
		return messageQueueProvider;
	}
}
