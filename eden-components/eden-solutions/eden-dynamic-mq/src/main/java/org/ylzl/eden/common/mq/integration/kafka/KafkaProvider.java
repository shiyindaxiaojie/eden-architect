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

package org.ylzl.eden.common.mq.integration.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.ylzl.eden.common.mq.MessageQueueProvider;
import org.ylzl.eden.common.mq.MessageQueueType;
import org.ylzl.eden.common.mq.model.Message;
import org.ylzl.eden.common.mq.producer.MessageSendCallback;
import org.ylzl.eden.common.mq.producer.MessageSendException;
import org.ylzl.eden.common.mq.producer.MessageSendResult;

/**
 * Kafka 生产者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
public class KafkaProvider implements MessageQueueProvider {

	private static final String KAFKA_PROVIDER_SEND_INTERRUPTED = "KafkaProvider send interrupted: {}";

	private static final String KAFKA_PROVIDER_CONSUME_ERROR = "KafkaProvider send error: {}";

	private final KafkaTemplate<String, String> kafkaTemplate;

	/**
	 * 消息类型
	 *
	 * @return 消息类型
	 */
	@Override
	public String messageQueueType() {
		return MessageQueueType.KAFKA.name();
	}

	/**
	 * 同步发送消息
	 *
	 * @param message
	 * @return
	 */
	@Override
	public MessageSendResult syncSend(Message message) {
		try {
			ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message.getTopic(), message.getBody());
			SendResult<String, String> sendResult = future.get();
			return transfer(sendResult);
		} catch (InterruptedException e) {
			log.error(KAFKA_PROVIDER_SEND_INTERRUPTED, e.getMessage(), e);
			Thread.currentThread().interrupt();
			throw new MessageSendException(e.getMessage());
		} catch (Exception e) {
			log.error(KAFKA_PROVIDER_CONSUME_ERROR, e.getMessage(), e);
			throw new MessageSendException(e.getMessage());
		}
	}

	/**
	 * 异步发送消息
	 *
	 * @param message
	 * @param messageCallback
	 */
	@Override
	public void asyncSend(Message message, MessageSendCallback messageCallback) {
		try {
			ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message.getTopic(), message.getBody());
			future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

				@Override
				public void onSuccess(SendResult<String, String> sendResult) {
					messageCallback.onSuccess(transfer(sendResult));
				}

				@Override
				public void onFailure(Throwable e) {
					messageCallback.onFailed(e);
				}
			});
		} catch (Exception e) {
			log.error(KAFKA_PROVIDER_CONSUME_ERROR, e.getMessage(), e);
			throw new MessageSendException(e.getMessage());
		}
	}

	/**
	 * 转化为自定义的 MessageSendResult
	 *
	 * @param sendResult
	 * @return
	 */
	private MessageSendResult transfer(SendResult<String, String> sendResult) {
		ProducerRecord<String, String> producerRecord = sendResult.getProducerRecord();
		RecordMetadata recordMetadata = sendResult.getRecordMetadata();
		return MessageSendResult.builder()
			.topic(producerRecord.topic())
			.partition(recordMetadata.partition())
			.offset(recordMetadata.offset())
			.build();
	}
}
