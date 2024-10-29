/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.common.mq.spring.boot.support;

import lombok.RequiredArgsConstructor;
import org.ylzl.eden.common.mq.MessageQueueProvider;
import org.ylzl.eden.spring.framework.beans.ApplicationContextHelper;

import java.util.Map;

/**
 * 消息队列帮助支持
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
public class MessageQueueHelper {

	private static final String MQ_TYPE_NOT_FOUND = "Message queue type named '{}' not found";

	private final String primary;

	public MessageQueueProvider getBean() {
		return getBean(primary);
	}

	public MessageQueueProvider getBean(String messageQueueType) {
		Map<String, MessageQueueProvider> messageQueueProviders =
			ApplicationContextHelper.getBeansOfType(MessageQueueProvider.class);
		return messageQueueProviders.values().stream()
			.filter(predicate -> predicate.messageQueueType().equalsIgnoreCase(messageQueueType))
			.findFirst()
			.orElseThrow(() -> new RuntimeException(MQ_TYPE_NOT_FOUND));
	}
}
