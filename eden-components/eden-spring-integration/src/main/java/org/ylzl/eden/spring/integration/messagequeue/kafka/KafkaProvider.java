package org.ylzl.eden.spring.integration.messagequeue.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.ylzl.eden.spring.integration.messagequeue.core.Message;
import org.ylzl.eden.spring.integration.messagequeue.core.MessageQueueProvider;
import org.ylzl.eden.spring.integration.messagequeue.core.producer.MessageQueueProducerException;
import org.ylzl.eden.spring.integration.messagequeue.core.producer.MessageSendCallback;
import org.ylzl.eden.spring.integration.messagequeue.core.producer.MessageSendResult;

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
	 * 同步发送消息
	 *
	 * @param message
	 * @return
	 * @throws MessageQueueProducerException
	 */
	@Override
	public MessageSendResult syncSend(Message message) throws MessageQueueProducerException {
		try {
			ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message.getTopic(), message.getBody());
			SendResult<String, String> sendResult = future.get();
			return transfer(sendResult);
		} catch (InterruptedException e) {
			log.error(KAFKA_PROVIDER_SEND_INTERRUPTED, e.getMessage(), e);
			Thread.currentThread().interrupt();
			throw new MessageQueueProducerException(e.getMessage());
		} catch (Exception e) {
			log.error(KAFKA_PROVIDER_CONSUME_ERROR, e.getMessage(), e);
			throw new MessageQueueProducerException(e.getMessage());
		}
	}

	/**
	 * 异步发送消息
	 *
	 * @param message
	 * @param messageCallback
	 * @throws MessageQueueProducerException
	 */
	@Override
	public void asyncSend(Message message, MessageSendCallback messageCallback) throws MessageQueueProducerException {
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
			throw new MessageQueueProducerException(e.getMessage());
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
