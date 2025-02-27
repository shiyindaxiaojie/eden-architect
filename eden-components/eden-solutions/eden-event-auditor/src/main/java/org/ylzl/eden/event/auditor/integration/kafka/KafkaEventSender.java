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

package org.ylzl.eden.event.auditor.integration.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.ylzl.eden.event.auditor.EventSender;
import org.ylzl.eden.event.auditor.model.AuditingEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于 Kafka 发送审计事件
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
public class KafkaEventSender implements EventSender {

	private static final String KAFKA_SEND_AUDIT_EVENT_SUCCESS = "KafkaTemplate send audit event success, message: {}";

	private static final String KAFKA_SEND_AUDIT_EVENT_FAILED = "KafkaTemplate send audit event failed, message: {}";

	private static final String KAFKA_SEND_AUDIT_EVENT_ERROR = "KafkaTemplate send audit event error";

	private final KafkaTemplate<String, String> kafkaTemplate;

	private final String topic;

	/**
	 * 发送审计事件列表
	 *
	 * @param events 审计事件列表
	 */
	@Override
	public void send(List<AuditingEvent> events) {
		List<String> messages = events.stream()
			.map(AuditingEvent::getContent).collect(Collectors.toList());
		messages.forEach(
			message -> {
				try {
					ListenableFuture<SendResult<String, String>> future =
						kafkaTemplate.send(topic, message);
					future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

						@Override
						public void onSuccess(SendResult<String, String> sendResult) {
							log.debug(KAFKA_SEND_AUDIT_EVENT_SUCCESS, message);
						}

						@Override
						public void onFailure(Throwable e) {
							log.warn(KAFKA_SEND_AUDIT_EVENT_FAILED, message, e);
						}
					});
				} catch (Exception e) {
					log.error(KAFKA_SEND_AUDIT_EVENT_ERROR, e);
				}
			}
		);
	}
}
