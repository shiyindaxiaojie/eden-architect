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

package org.ylzl.eden.common.mq;

import org.ylzl.eden.common.mq.model.Message;
import org.ylzl.eden.common.mq.producer.MessageSendCallback;
import org.ylzl.eden.common.mq.producer.MessageSendResult;

/**
 * 消息队列生产者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface MessageQueueProvider {

	/**
	 * 消息类型
	 *
	 * @return 消息类型
	 */
	String messageQueueType();

	/**
	 * 同步发送消息
	 *
	 * @param message
	 * @return
	 */
	MessageSendResult syncSend(Message message);

	/**
	 * 异步发送消息
	 *
	 * @param message
	 * @param messageCallback
	 */
	void asyncSend(Message message, MessageSendCallback messageCallback);
}
