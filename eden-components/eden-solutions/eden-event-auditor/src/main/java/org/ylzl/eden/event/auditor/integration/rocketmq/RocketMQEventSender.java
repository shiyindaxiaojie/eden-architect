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

package org.ylzl.eden.event.auditor.integration.rocketmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.ylzl.eden.event.auditor.EventSender;
import org.ylzl.eden.event.auditor.model.AuditingEvent;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于 RocketMQ 发送审计事件
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class RocketMQEventSender implements EventSender {

	private static final String ROCKETMQ_SEND_AUDIT_EVENT_SUCCESS = "RocketMQTemplate send audit event success, message: {}";

	private static final String ROCKETMQ_SEND_AUDIT_EVENT_FAILED = "RocketMQTemplate send audit event failed, message: {}";

	private static final String ROCKETMQ_SEND_AUDIT_EVENT_ERROR = "RocketMQTemplate send audit event error";

	private final RocketMQTemplate rocketMQTemplate;

	private final String topic;

	private final String namespace;

	private final String tags;

	private final String keys;

	/**
	 * 发送审计事件列表
	 *
	 * @param events 审计事件列表
	 */
	@Override
	public void send(List<AuditingEvent> events) {
		DefaultMQProducer producer = rocketMQTemplate.getProducer();
		if (StringUtils.isNotBlank(namespace)) {
			producer.setNamespace(namespace);
		}

		List<String> messages = events.stream()
			.map(AuditingEvent::getContent).collect(Collectors.toList());
		messages.forEach(
			message -> {
				Message rocketMsg = new Message(topic, tags, keys, message.getBytes(StandardCharsets.UTF_8));
				try {
					producer.send(rocketMsg, new SendCallback() {

						@Override
						public void onSuccess(SendResult sendResult) {
							log.debug(ROCKETMQ_SEND_AUDIT_EVENT_SUCCESS, message);
						}

						@Override
						public void onException(Throwable e) {
							log.warn(ROCKETMQ_SEND_AUDIT_EVENT_FAILED, message, e);
						}
					});
				} catch (InterruptedException e) {
					log.error(ROCKETMQ_SEND_AUDIT_EVENT_ERROR, e);
					Thread.currentThread().interrupt();
				} catch (Exception e) {
					log.error(ROCKETMQ_SEND_AUDIT_EVENT_ERROR, e);
				}
			}
		);
	}
}
